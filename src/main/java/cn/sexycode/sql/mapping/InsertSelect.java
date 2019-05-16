package cn.sexycode.sql.mapping;

import cn.sexycode.sql.MySqlException;
import cn.sexycode.sql.dialect.Dialect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of InsertSelect.
 *
 */
public class InsertSelect {
	private Dialect dialect;
	private String tableName;
	private String comment;
	private List columnNames = new ArrayList();
	private Select select;

	public InsertSelect(Dialect dialect) {
		this.dialect = dialect;
	}

	public InsertSelect setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public InsertSelect setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public InsertSelect addColumn(String columnName) {
		columnNames.add( columnName );
		return this;
	}

	public InsertSelect addColumns(String[] columnNames) {
		for ( int i = 0; i < columnNames.length; i++ ) {
			this.columnNames.add( columnNames[i] );
		}
		return this;
	}

	public InsertSelect setSelect(Select select) {
		this.select = select;
		return this;
	}

	public String toStatementString() {
		if ( tableName == null ) {
			throw new MySqlException( "no table name defined for insert-select" );
		}
		if ( select == null ) {
			throw new MySqlException( "no select defined for insert-select" );
		}

		StringBuilder buf = new StringBuilder( (columnNames.size() * 15) + tableName.length() + 10 );
		if ( comment!=null ) {
			buf.append( "/* " ).append( comment ).append( " */ " );
		}
		buf.append( "insert into " ).append( tableName );
		if ( !columnNames.isEmpty() ) {
			buf.append( " (" );
			Iterator itr = columnNames.iterator();
			while ( itr.hasNext() ) {
				buf.append( itr.next() );
				if ( itr.hasNext() ) {
					buf.append( ", " );
				}
			}
			buf.append( ")" );
		}
		buf.append( ' ' ).append( select.toStatementString() );
		return buf.toString();
	}
}