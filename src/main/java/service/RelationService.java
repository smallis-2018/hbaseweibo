package service;

import com.alibaba.fastjson.JSON;
import dao.RelationDAO;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.*;
import until.HBaseCon;

import java.io.IOException;
import java.util.NavigableMap;
import java.util.TreeMap;

public class RelationService {
    private HBaseCon hBaseCon;
    private Connection connection;
    private RelationDAO crud;

    public RelationService() {
        hBaseCon = new HBaseCon();
        connection = hBaseCon.getConnection();
        crud = new RelationDAO(connection);
    }

    public TreeMap<String,String> getUserBaseInfo(String id){
        TreeMap<String,String> baseMap = new TreeMap<String, String>();
        try {
            Result result = crud.get(id,"base");
            if (result != null) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    String userId = new String(CellUtil.cloneRow(cell));
                    String userName = new String(CellUtil.cloneValue(cell));
                    baseMap.put(userId, userName);
                }
            }else {
                baseMap.put("","");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseMap;
    }

    //取关注列表
    public TreeMap<String,String> getFollow(String id){
        TreeMap<String,String> followMap = new TreeMap<String, String>();
        try {
            Result result = crud.get(id,"follow");
            if (result != null) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    String userId = new String(CellUtil.cloneRow(cell));
                    String userName = new String(CellUtil.cloneValue(cell));
                    followMap.put(userId, userName);
                }
            }else {
                followMap.put("","");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return followMap;
    }

    //取关注列表
    public TreeMap<String,String> getFans(String id){
        TreeMap<String,String> fansMap = new TreeMap<String, String>();
        try {
            Result result = crud.get(id,"fans");
            if (result != null) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    String userId = new String(CellUtil.cloneRow(cell));
                    String userName = new String(CellUtil.cloneValue(cell));
                    fansMap.put(userId, userName);
                }
            }else {
                fansMap.put("","");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fansMap;
    }



    //TODO 实现陌生人列表（即没有关注的人）
    public TreeMap<String, String> getStranger(String id) {
        TreeMap<String,String> strangerMap = new TreeMap<String, String>();
        Filter filter = new QualifierFilter(
                CompareOperator.NOT_EQUAL,
                new BinaryComparator(id.getBytes())
        );
        try {
            ResultScanner resultScanner = crud.scan(filter);

            if (resultScanner != null) {
                for (Result result:resultScanner){
                    Cell[] cells = result.rawCells();
                    for (Cell cell : cells) {
                        String getid = new String(CellUtil.cloneQualifier(cell));
                        String getName = new String(CellUtil.cloneValue(cell));
                        if(!getid.equals("name")){
                            strangerMap.put(getid,getName);
                        }
                    }
                }
            }else {
                strangerMap.put("","");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strangerMap;
    }
}
