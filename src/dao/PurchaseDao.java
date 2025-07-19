/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import connection.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Amna Nadeem
 */
public class PurchaseDao {

    Connection con = MyConnection.getConnection();
    PreparedStatement ps;
    Statement st;
    ResultSet rs;

    public PurchaseDao() throws SQLException {
        this.con = MyConnection.getConnection();
    }

    // get purchase table max row
    public int getMaxRow() {
        int row = 0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("select max(id) from purchase");
            while (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return row + 1;
    }

    public double getTodaySalesAmount() {
        double row = 0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("select sum(total) from purchase where date(p_date) = curdate()");
            while (rs.next()) {
                row = rs.getDouble(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return row;
    }

    public double getTotalSalesAmount() {
        double row = 0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("select sum(total) from purchase");
            while (rs.next()) {
                row = rs.getDouble(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return row;
    }

    public int getTodaySalesCount() {
        int row = 0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("select count(*) from purchase where date(p_date) = curdate()");
            while (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return row;
    }

    public int getTotalSalesCount() {
        int row = 0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("select count(*) from purchase");
            while (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return row;
    }

    public int getPurchaseByUserCount(int user_id) {
        int rowCount = 0;
        try {
            ps = con.prepareStatement("select count(*) from purchase where uid = ?");
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rowCount = rs.getInt(1); // get the count from first column
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return rowCount;
    }

    //get user value
    public String[] getUserValue(String email) {
        String[] value = new String[5];
        String sql = "select uid,uname,uphone,uaddress1,uaddress2 from user where uemail = '" + email + "'";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                value[0] = rs.getString(1);
                value[1] = rs.getString(2);
                value[2] = rs.getString(3);
                value[3] = rs.getString(4);
                value[4] = rs.getString(5);

            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;

    }

    //insert data into purchase table
    public void insert(int id, int uid, String uName,
            String uPhone, int pid, String pname, int qty,
            double price, double total, String pDate, String address,
            String rDate, String supplier, String status) {
        String sql = "insert into purchase values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, uid);
            ps.setString(3, uName);
            ps.setString(4, uPhone);
            ps.setInt(5, pid);
            ps.setString(6, pname);
            ps.setInt(7, qty);
            ps.setDouble(8, price);
            ps.setDouble(9, total);
            ps.setString(10, pDate);
            ps.setString(11, address);
            ps.setString(12, rDate);
            ps.setString(13, supplier);
            ps.setString(14, status);
            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //get product quantity
    public int getQty(int pid) {
        int qty = 0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("select pqty from product where pid = " + pid + "");
            if (rs.next()) {
                qty = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return qty;
    }

    //update product quantity
    public void qtyUpdate(int pid, int qty) {
        String sql = "update product set pqty = ? where pid = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, qty);
            ps.setInt(2, pid);
            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //get specified user purchased products
    public void getProducts(JTable table, String search, int uid) {
        String sql = "select * from purchase where concat(id,pid, product_name) like ? and uid = ? order by id desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + search + "%");
            ps.setInt(2, uid);
            rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[10];
                row[0] = rs.getInt(1);
                row[1] = rs.getInt(5);
                row[2] = rs.getString(6);
                row[3] = rs.getInt(7);
                row[4] = rs.getDouble(8);
                row[4] = rs.getDouble(5);
                row[5] = rs.getDouble(9);
                row[6] = rs.getString(10);
                row[7] = rs.getString(12);
                row[8] = rs.getString(13);
                row[9] = rs.getString(14);

                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refund(int id) {
        int x = JOptionPane.showConfirmDialog(null, "Are you sure to refund this product", "Refund Product", 2);
        if (x == JOptionPane.OK_OPTION) {

            try {
                ps = con.prepareStatement("delete from purchase where id = ?");
                ps.setInt(1, id);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(null, "Product refund succeed");

                }
            } catch (SQLException ex) {
                Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void getPurchaseData(JTable table, String search) {
        String sql = "select * from purchase where concat(id, pid, uname, product_name) like ? order by id desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + search + "%");
            rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[8];
                row[0] = rs.getInt(1);
                row[1] = rs.getInt(2);
                row[2] = rs.getInt(5);
                row[3] = rs.getInt(7);
                row[4] = rs.getDouble(8);
                row[5] = rs.getDouble(9);
                row[6] = rs.getString(12);
                row[7] = rs.getString(13);

                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
