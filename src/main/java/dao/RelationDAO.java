package dao;

import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RelationDAO {
    private static final String tableName = "sm:relation";

    private Connection connection;

    public RelationDAO(Connection connection) {
        this.connection = connection;
    }

    //put操作
    public void put(String rowKey, String columnFamilyName, String columnName, String value) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Put p = new Put(rowKey.getBytes());
        p.addColumn(columnFamilyName.getBytes(), columnName.getBytes(), value.getBytes());
        table.put(p);
    }


    public ResultScanner scan(Filter filter, String[] familyNames, String[] columnNames) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.setFilter(filter);
        if (familyNames.length != columnNames.length) {
            return null;
        }
        for (int i = 0; i < familyNames.length; i++)
            scan.addColumn(familyNames[i].getBytes(), columnNames[i].getBytes());
        return table.getScanner(scan);
    }

    //带过滤器的扫描方法
    public ResultScanner scan(Filter filter) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.setFilter(filter);
        return table.getScanner(scan);
    }

    public ResultScanner scan(FilterList filterList, String familyName, String columnName) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.addColumn(familyName.getBytes(), columnName.getBytes());
        scan.setFilter(filterList);
        return table.getScanner(scan);
    }

    //行键和列族获取数据
    public Result get(String rowKey, String familyName) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(rowKey.getBytes());
        get.addFamily(familyName.getBytes());
        return table.get(get);
    }

    public Result get(String rowKey) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(rowKey.getBytes());
        return table.get(get);
    }

    public Result get(String rowKey, String familyName, String columnName) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(rowKey.getBytes());
        get.addColumn(familyName.getBytes(),columnName.getBytes());
        return table.get(get);
    }

    public void delete(String rowKey) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(rowKey.getBytes());
        table.delete(delete);
    }

    public void delete(String rowKey, String familyName, String columnName) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(rowKey.getBytes());
        delete.addColumn(familyName.getBytes(), columnName.getBytes());
        table.delete(delete);
    }


}
