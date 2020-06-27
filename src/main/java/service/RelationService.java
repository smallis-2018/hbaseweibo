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
import org.apache.hadoop.hbase.util.AtomicUtils;
import org.apache.zookeeper.Transaction;
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

    public boolean dounfollow(String id, String followId){
        try {
            crud.delete(id,"follow",followId);
            crud.delete(followId,"fans",id);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
                    String userId = new String(CellUtil.cloneQualifier(cell));
                    String userName = new String(CellUtil.cloneValue(cell));
                    if(!userId.equals("name")){
                        followMap.put(userId, userName);
                    }
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
        TreeMap<String,String> followMap;
        //获取关注列表
        followMap = getFollow(id);
        //设置个AND过滤器表
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        //将关注列表里的ID都加入行键过滤器
        for(String s : followMap.keySet()){
            Filter filter = new RowFilter(
                    CompareOperator.NOT_EQUAL,
                    new BinaryComparator(s.getBytes())
            );
            filterList.addFilter(filter);
        }

        //把自己的ID也放进过滤器
        Filter filter = new RowFilter(
                CompareOperator.NOT_EQUAL,
                new BinaryComparator(id.getBytes())
        );
        filterList.addFilter(filter);

        try {
            ResultScanner resultScanner = crud.scan(filterList, "base", "name");
            if (resultScanner != null) {
                for (Result result:resultScanner){
                    Cell[] cells = result.rawCells();
                    for (Cell cell : cells) {
                        String getid = new String(CellUtil.cloneRow(cell));
                        String getName = new String(CellUtil.cloneValue(cell));
                        strangerMap.put(getid,getName);
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
