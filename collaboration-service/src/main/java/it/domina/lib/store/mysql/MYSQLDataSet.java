package it.domina.lib.store.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.lib.store.Condition;
import it.domina.lib.store.ConditionType;
import it.domina.lib.store.DataSet;
import it.domina.lib.store.Field;
import it.domina.lib.store.FieldAction;
import it.domina.lib.store.FieldType;
import it.domina.lib.store.Filter;
import it.domina.lib.store.GroupBy;
import it.domina.lib.store.OrderBy;
import it.domina.lib.store.Row;

public class MYSQLDataSet implements DataSet {

	private static final Logger logger = Logger.getLogger(MYSQLConnectionPool.class);
	
	private	String						sql;
	private List<Field>					params;
	
	private MYSQLConnectionPool 		pool = null;
	private Filter						filtro;

	private Integer   					size = null;
	private Integer 					cursor;
	private Integer 					position = null;
	private Integer 					pageSize = 101;

	private List<Row> 					table = null;
	
	public MYSQLDataSet(MYSQLConnectionPool pool, Filter f){
		this.pool = pool;
		this.filtro = f;
		this.table = new Vector<Row>();
		createSql();
		loadMap();
	}
	
	@Override
	public int size() {
		if (this.size == null){
			this.size = getSizeResultSet();
		}
		return this.size;
	}

	@Override
	public boolean next() {
		
		try {
			if ((this.position + this.cursor +1 ) ==  this.size){
				return false;
			}
			else{
				if ((this.cursor+1) == this.pageSize){
					if ((this.position + this.pageSize) > this.size) {
						this.position = this.size-this.position;
					}
					else if ((this.position + this.pageSize)< this.size) {
						this.position = this.position + this.pageSize;
					}
					loadMap();
				}
				this.cursor++;
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean previous() {

		try {
			if (this.cursor == 1){
				if ((this.position-this.pageSize) < 1) {
					this.position = 1;
				}
				else{
					this.position = this.position - this.pageSize;
				}
				loadMap();
				this.cursor = 0;
			}
			else{
				this.cursor--;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean last() {

		try {
			this.position = this.size - this.pageSize;
			loadMap();
			this.cursor = 0;
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public boolean first() {
		try {
			this.position = 0;
			loadMap();
			this.cursor = 0;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public boolean beforeFirst(){
		try {
			this.position = 0;
			loadMap();
			this.cursor = -1;
			this.position = -1;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public boolean moveTo(int pos) {
		
		try {
			if ((pos+1) >  this.size){
				return false;
			}
			else{
				this.position = pos;
				loadMap();
				this.cursor = 0;
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Row getData() {
		return this.table.get(this.cursor);
	}
	
	@Override
	public Row getDataAt(int pos) {
		if (pos<0){
			this.position = -1;
			this.cursor = this.position;
			return null;
		}
		else{
			if ((pos>=this.position+this.pageSize) || (pos<this.position)){
				if (this.pageSize==0){
					return null;
				}
				else{
					this.position = (pos/this.pageSize)*this.pageSize;
				}
				loadMap();
			}
			this.cursor = pos - this.position;
			return this.table.get(this.cursor);
		}
	}

	private void createSql(){

		if (this.filtro.getColumnsName().size()>0){
			Boolean isFirst = true;
			for (String col: this.filtro.getColumnsName()){
				if (isFirst){
					this.sql = "SELECT " + col;
					isFirst = false;
				}
				else{
					this.sql += "," + col;
				}
			}
		}
		else{
			this.sql = "SELECT * ";
		}

		this.sql += " FROM " + this.filtro.getFromCondition();

		this.params = new ArrayList<Field>();
		String sWhere = "";
		Boolean first = true;
		if (this.filtro.getWhere()!=null){
			if (this.filtro.getWhere().size()>0){
				Iterator<Condition> iWhere = this.filtro.getWhere().iterator();
				while (iWhere.hasNext()){
					if (first){
						sWhere = " WHERE ";
						first = false;
					}
					else{
						sWhere += " AND ";
					}
					Condition c = iWhere.next();
					if (c.getModo()==ConditionType.ISNULL){
						sWhere += c.getKey()+ " " + c.getModo().toString();
					}else if (c.getModo()==ConditionType.ISNOTNULL){
						sWhere += c.getKey()+ " " + c.getModo().toString();
					}else if (c.getModo()==ConditionType.CUSTOM){
						sWhere += c.getKey()+ " " + c.getValue();
					}else{
						sWhere += c.getKey()+ " " + c.getModo().toString() + " ?";
					}
					Field cn = null;
					switch (c.getModo()) {
					case LIKE:
						cn = new Field(c.getKey(), "%"+c.getValue()+"%", FieldAction.NONE);
						this.params.add(cn);
						break;
					case START_WITH:
						cn = new Field(c.getKey(), c.getValue()+"%", FieldAction.NONE);
						this.params.add(cn);
						break;
					case END_WITH:
						cn = new Field(c.getKey(), "%"+c.getValue(), FieldAction.NONE);
						this.params.add(cn);
						break;
					case ISNULL:
						break;
					case ISNOTNULL:
						break;
					case CUSTOM:
						break;
					default:
						FieldType tipo = MYSQLDB.readTipo(c.getValue());
						cn = new Field(c.getKey(), tipo, c.getValue(), FieldAction.NONE);
						this.params.add(cn);
						break;
					}
				}
			}
		}
		if (!sWhere.equals("")){
			this.sql += " "+sWhere;
		}
		String sGroupBy = "";
		first = true;
		if (this.filtro.getGroupBy()!=null){
			if (this.filtro.getGroupBy().size()>0){
				Iterator<GroupBy> iGroup = this.filtro.getGroupBy().iterator();
				while (iGroup.hasNext()){
					if (first){
						sGroupBy = " GROUP BY ";
						first = false;
					}
					else{
						sGroupBy += ", ";
					}
					GroupBy o = iGroup.next();
					sGroupBy += o.getKey();
				}
				this.sql += " "+sGroupBy;
			}
		}		
		
		String sHaving = "";
		first = true;
		if (this.filtro.getHaving()!=null){
			if (this.filtro.getHaving().size()>0){
				Iterator<Condition> iHaving = this.filtro.getHaving().iterator();
				while (iHaving.hasNext()){
					if (first){
						sHaving = " HAVING ";
						first = false;
					}
					else{
						sHaving += " AND ";
					}
					Condition c = iHaving.next();
					if (c.getModo()==ConditionType.ISNULL){
						sHaving += c.getKey()+ " " + c.getModo().toString();
					}else if (c.getModo()==ConditionType.ISNOTNULL){
						sHaving += c.getKey()+ " " + c.getModo().toString();
					}else if (c.getModo()==ConditionType.CUSTOM){
						sHaving += c.getKey()+ " " + c.getValue();
					}else{
						sHaving += c.getKey()+ " " + c.getModo().toString() + " ?";
					}
					Field cn = null;
					switch (c.getModo()) {
					case LIKE:
						cn = new Field(c.getKey(), "%"+c.getValue()+"%", FieldAction.NONE);
						this.params.add(cn);
						break;
					case START_WITH:
						cn = new Field(c.getKey(), c.getValue()+"%", FieldAction.NONE);
						this.params.add(cn);
						break;
					case END_WITH:
						cn = new Field(c.getKey(), "%"+c.getValue(), FieldAction.NONE);
						this.params.add(cn);
						break;
					case ISNULL:
						break;
					case ISNOTNULL:
						break;
					case CUSTOM:
						break;
					default:
						FieldType tipo = MYSQLDB.readTipo(c.getValue());
						cn = new Field(c.getKey(), tipo, c.getValue(), FieldAction.NONE);
						this.params.add(cn);
						break;
					}
				}
			}
		}
		if (!sHaving.equals("")){
			this.sql += " "+sHaving;
		}

		/*
		String sHaving = "";
		first = true;
		if (this.filtro.getHaving()!=null){
			if (this.filtro.getHaving().size()>0){
				Iterator<Having> iHaving = this.filtro.getHaving().iterator();
				while (iHaving.hasNext()){
					if (first){
						sHaving = " HAVING ";
						first = false;
					}
					else{
						sHaving += ", ";
					}
					Having h = iHaving.next();
					sHaving += h.getKey();
				}
				this.sql += " "+sHaving;
			}
		}		
		*/

		String sOrderBy = "";
		first = true;
		if (this.filtro.getOrderBy()!=null){
			if (this.filtro.getOrderBy().size()>0){
				
				Iterator<OrderBy> iOrder = this.filtro.getOrderBy().iterator();
				while (iOrder.hasNext()){
					if (first){
						sOrderBy = " ORDER BY ";
						first = false;
					}
					else{
						sOrderBy += ", ";
					}
					OrderBy o = iOrder.next();
					sOrderBy += o.getKey() + " " + o.getDirection().toString();
				}
				this.sql += " "+sOrderBy;
			}
		}		
	}
	
	private void loadMap(){

		PreparedStatement 	stm = null;
		ResultSet			rs = null;
		MYSQLConnection 		con = null;

		try {
			con = this.pool.newConnection();
			this.pageSize = con.getPageSize();
			stm = con.prepareStatement(this.sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stm = MYSQLDB.parametri(stm, this.params);
			stm.executeQuery();
			this.size = getSizeResultSet();
			if (this.position!=null){
				if (this.size<this.position){
					this.position = 0;
				}
			}
			rs = stm.getResultSet();
			rs.beforeFirst();
			this.cursor = -1;
			if (rs!=null){
				this.table = buildListOfMapRecords(rs);
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
		} finally{
			try {
				if (rs!=null){
					rs.close();
				}
				stm.close();
			} catch (SQLException e) {
			}
			con.close();
		}
			
	}
	
	private List<Row> buildListOfMapRecords( ResultSet rs ) throws Exception {

		List<Row> l = new LinkedList<Row>();
		if( rs != null ) {
			if (this.position !=null){
				if (this.position == 0){
					rs.beforeFirst();
				}
				else{
					rs.absolute(this.position);
				}
			}
			else{
				this.position = 0;
				rs.beforeFirst();
			}
			if ((this.size-this.position)<=this.pageSize){
				this.pageSize = this.size-this.position;
			}
			for (int j=1;j<=this.pageSize;j++){
				rs.next();
				Row r = new Row();
				ResultSetMetaData mtData = rs.getMetaData();
				Integer iColNum = mtData.getColumnCount()+1; 
				for (int i = 1; i < iColNum; i++) {
					r.addField(new MYSQLField(i, mtData, rs.getObject(i)));
				}
				l.add(r);
			}
		}
		return l;
	}		

	private Integer getSizeResultSet(){
		
		String subSql = "";
		if (this.sql.indexOf("ORDER BY") >0 ){
			subSql = this.sql.substring(0, this.sql.indexOf("ORDER BY"));
		}
		else{
			subSql = this.sql;
		}
		String sSql = "SELECT COUNT(*) AS RIGHE FROM (" + subSql + ") AS COUNTTABLE ";
		PreparedStatement pst = null;
		ResultSet r = null;
		MYSQLConnection 		con = null;
		
		try {
			con = this.pool.newConnection();
			pst = con.prepareStatement(sSql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pst = MYSQLDB.parametri(pst, this.params);
			r = pst.executeQuery();
			if (r.next()){
				return r.getInt("RIGHE");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (pst != null) {
				try {
					r.close();
					pst.close();
				} catch (SQLException e) {
				}
				con.close();
			};
		}

		return 0;
	}

}
