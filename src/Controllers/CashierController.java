/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.CashierModel;
import Models.CustomerModel;
import Models.ProductModel;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author benza
 */
public class CashierController {
    private Connection con;
    public CashierController(Connection con){
        this.con = con;
    }
    
    
    public Object[] getProducts(String cat, String subcat){
        ArrayList<String> fetchedproducts = new ArrayList<String>();
        PreparedStatement stmt;
        try {
            stmt = this.con.prepareStatement("SELECT name, quantity, price, pictureurl FROM products WHERE category=? AND subcategory=?");
            stmt.setString(1,cat);
            stmt.setString(2,subcat);
             ResultSet res= stmt.executeQuery();
             while(res.next()){
                 fetchedproducts.add(res.getString("name"));
             }
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fetchedproducts.toArray();
    }
    
    public int getTotalPendingOrders() throws SQLException{
        PreparedStatement stmt= this.con.prepareStatement("SELECT COUNT(*) AS total FROM carts WHERE UPPER(status)='PENDING'");
        ResultSet res= stmt.executeQuery();
        res.next();
        return res.getInt("total");
    }
    
    public ResultSet getOrders(int prefix) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM carts WHERE UPPER(status)='PENDING' offset ? rows fetch first 4 rows only");
        stmt.setInt(1,prefix*4);
        return stmt.executeQuery();
    }
    
    public ProductModel getProduct(String pn) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM products WHERE name=?");
        stmt.setString(1, pn);
        return new ProductModel(stmt.executeQuery());
    }
    
    public ProductModel getProduct(int id) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM products WHERE id=?");
        stmt.setInt(1, id);
        return new ProductModel(stmt.executeQuery());
    }
    
    public Object[] getCustomersList(String like) throws SQLException{
        ArrayList<KeyValue> elm = new ArrayList<KeyValue>();
        like="%"+like.toUpperCase()+"%";
        String phone = like+"%";
        PreparedStatement stmt = this.con.prepareStatement("SELECT id,firstname,lastname FROM customers WHERE (UPPER(firstname) LIKE ? OR phonenumber LIKE ?) AND id<>0");
        stmt.setString(1, like);
        stmt.setString(2, phone);
        ResultSet res= stmt.executeQuery();
        while(res.next()){
            elm.add(new KeyValue(res.getInt("id"),res.getString("firstname")));
        }
        return elm.toArray();
    }
    
    public String getCustomerStars(int id) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT COUNT(*) AS stars FROM carts WHERE customerid=? AND UPPER(status)='PAID'");
        stmt.setInt(1, id);
        ResultSet res = stmt.executeQuery();
        res.next();
        return String.valueOf(res.getInt("stars"));
    }
    
    public int getLastCart() throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT id FROM carts",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        ResultSet res= stmt.executeQuery();
        res.last();
        return res.getInt("id");
    }
    
    public int getLastPaidCart() throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT id FROM carts WHERE UPPER(status)='PAID'",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        ResultSet res= stmt.executeQuery();
        res.last();
        return res.getInt("id");
    }
    
    public int getCustomerCart(int customer_id) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT id FROM carts WHERE customerid=? AND UPPER(status)='PENDING'",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        stmt.setInt(1, customer_id);
        ResultSet res= stmt.executeQuery();
        if(res.next()){
            res.last();
            return res.getInt("id");
        }
        return 0;
    }
    
    public int getCartByTime(java.sql.Date date) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT id FROM carts WHERE creationdate=?");
        stmt.setDate(1, date);
        ResultSet res= stmt.executeQuery();
        res.last();
        return res.getInt("id");
    }
    
    public void saveOrder(ArrayList<int[]> orders, String pType, double pTotal, String pStatus, int customer_id) throws SQLException{
        int referenceCart;
        if(customer_id!=0 && getCustomerCart(customer_id)!=0){
            PreparedStatement stmt = this.con.prepareStatement("UPDATE carts SET paymenttype=? , total=total+? , status=? WHERE id=? ");
            stmt.setString(1, pType);
            stmt.setDouble(2, pTotal);
            stmt.setString(3,pStatus);
            stmt.setInt(4, getCustomerCart(customer_id));
            stmt.executeUpdate();
        }else{
            java.sql.Date cd=new java.sql.Date(new java.util.Date().getTime());
            PreparedStatement stmt = this.con.prepareStatement("INSERT INTO carts (paymenttype,creationdate,total,status,customerid) VALUES (?,?,?,?,?)");
            stmt.setString(1, pType);
            stmt.setDate(2, cd);
            stmt.setDouble(3, pTotal);
            stmt.setString(4,pStatus);
            stmt.setInt(5, customer_id);
            stmt.executeUpdate();
        }
        referenceCart=getCustomerCart(customer_id);
        PreparedStatement stmt2 = this.con.prepareStatement("INSERT INTO orders (quantity,productid,cartid) VALUES (?,?,?)");
        PreparedStatement stmt3 = this.con.prepareStatement("UPDATE products SET quantity=quantity-? WHERE id=?");
        for(int[] order: orders){
            stmt2.setInt(1, order[1]);
            stmt3.setInt(1, order[1]);
            stmt2.setInt(2, order[0]);
            stmt3.setInt(2, order[0]);
            stmt2.setInt(3, referenceCart==0?getLastCart():referenceCart);
            stmt2.executeUpdate();
            stmt3.executeUpdate();
        }
    }
    
    public void savePendingOrder(int cartId, String pType) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("UPDATE carts SET paymenttype=? , status='paid' WHERE id=?");
        stmt.setString(1, pType);
        stmt.setInt(2, cartId);
        stmt.executeUpdate();
    }
    
    public double getPendingCartTotal(int id) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT total FROM carts WHERE customerid=? AND UPPER(status)='PENDING'");
        stmt.setInt(1, id);
        ResultSet res= stmt.executeQuery();
        if(res.next()){
            return res.getInt("total");
        }
        return 0;
    }
    
    public CustomerModel getCustomer(int id) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM customers WHERE id=?");
        stmt.setInt(1, id);
        return new CustomerModel(stmt.executeQuery());
    }
    
    /**
     * Get all the information related to an order displayed in the order menu
     * @param index Index of the order in the menu
     * @return ArraylList with 3 rows: 1. cart creation date, cart total. 2. Customer model. 3. ArrayList of products (1. Product Model. 2. quantity. 3. Trend for the graph.).
     * @throws SQLException 
     */
    public ArrayList<Object> getOrderInfo(int index) throws SQLException{
        ArrayList<Object> order= new ArrayList<Object>();
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM carts WHERE UPPER(status)='PENDING' offset ? rows fetch first row only");
        stmt.setInt(1, index);
        ResultSet res=  stmt.executeQuery();
        if(res.next()){
            order.add(new Object[] {res.getDate("creationdate"), res.getDouble("total")});
            order.add(getCustomer(res.getInt("customerid")));
            PreparedStatement stmt2= this.con.prepareStatement("SELECT * FROM orders WHERE cartid=?");
            stmt2.setInt(1,res.getInt("id"));
            ResultSet res2= stmt2.executeQuery();
            ArrayList<Object[]> products= new ArrayList<Object[]>();
            while(res2.next()){
                System.out.println("adding");
                products.add(new Object[] {getProduct(res2.getInt("productid")),res2.getInt("quantity"), getProductTrend(res.getInt("customerid"),res2.getInt("productid"))});
            }
            order.add(products);
        }
        return order;
    }
    
    public Object[] getProductTrend(int customerId, int productId) throws SQLException{
        XYSeries series1 = new XYSeries("First");
        XYSeriesCollection dataset = new XYSeriesCollection();
        PreparedStatement stmt= this.con.prepareStatement("SELECT quantity FROM carts, orders WHERE carts.customerid=? AND orders.productid=? AND orders.cartid=carts.id ORDER BY orders.id ASC",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        stmt.setInt(1, customerId);
        stmt.setInt(2, productId);
        ResultSet res= stmt.executeQuery();
        while(res.next()){
            series1.add(res.getRow(),res.getInt("quantity"));
        }
        dataset.addSeries(series1);
        res.last();
        double[] reg=new double[2];
        if(res.getRow()>1){
            reg=Regression.getOLSRegression((XYDataset)dataset, 0);
        }
        return new Object[] {(XYDataset)dataset, reg[1]};
    }
    
    public void printInvoice(int cartId, CashierModel cashier) throws FileNotFoundException, SQLException, DocumentException{
        //make sure that every line is 100 chars max
        ArrayList<Object> info= this.getOrderInfoByCartId(cartId);  
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("receipts/Order_"+String.valueOf((int)((Object[])info.get(0))[0])+".pdf"));
        Paragraph header = new Paragraph("POS\nOrder Receipt");
        Paragraph cart = new Paragraph(String.format("%-80s",((Date)((Object[])info.get(0))[2]).toString())+String.format("Cart number: %7s",String.valueOf(cartId)));
        Paragraph cashier_s = new Paragraph(String.format("%-100s","Cashier: "+cashier.getFullName()));
        if(info.get(1) instanceof CustomerModel){
            cashier_s = new Paragraph(String.format("Cashier: %-61s",cashier.getFullName())+String.format("Phone: %14s",cashier.phonenumber));
        }
        String products_txt="";
        for(Object[] product: (ArrayList<Object[]>)info.get(2)){
            ProductModel pp=(ProductModel)product[0];
            int pq=(int)product[1];
            products_txt+=String.format("%-77s", pp.name)+String.format("%6.2f x",pp.price)+String.format(" %-4d", pq)+String.format("%10.2f", pp.price*pq)+"\n";
        }
        products_txt+=String.format("%90s","")+String.format("Total: %10.2f",(double)((Object[])info.get(0))[3]);
        Paragraph products = new Paragraph(products_txt);
        Paragraph payment = new Paragraph(String.format("%-108s","Payment: "+(String)((Object[])info.get(0))[1]));
        Paragraph footer = new Paragraph(String.format("%-110s","Thank You."));
        if(info.get(1) instanceof CustomerModel){
            CustomerModel customer=((CustomerModel)info.get(1));
            footer = new Paragraph(String.format("%-46s","Thank You "+customer.getFullName()+".")+String.format("%46s","Your Points: "+getCustomerStars(customer.id)));
        }
        documentAdd(document,header,cart,cashier_s,products,payment,footer);
    }
    
    public void documentAdd(Document doc, Paragraph... items) throws DocumentException{
        Paragraph spacer = new Paragraph("----------------------------------------------------------------------------------------------------");
        spacer.setAlignment(Element.ALIGN_CENTER);
        doc.open();
        for(Paragraph item:items){
            item.setAlignment(Element.ALIGN_CENTER);
            doc.add(item);
            doc.add(spacer);
        }
        doc.close();
    }
    
    /**
     * Get information about a specific paid cart for printing.
     * @param cartId Paid Cart Id
     * @param cashier Cashier Model
     * @return ArrayList of:
     *  1. Cart info: cart id, payment type, data and total.
     *  2. Customer Model (if registered).
     *  3. Purchased products information:
     *     1. Product model.
     *     2. quantity.
     * @throws SQLException 
     */
    public ArrayList<Object> getOrderInfoByCartId(int cartId) throws SQLException{
        ArrayList<Object> order= new ArrayList<Object>();
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM carts WHERE id=?");
        stmt.setInt(1, cartId);
        ResultSet res=  stmt.executeQuery();
        if(res.next()){
            order.add(new Object[] {res.getInt("id"), res.getString("paymenttype"), res.getDate("creationdate"), res.getDouble("total")});
            order.add(res.getInt("customerid")==0?false:getCustomer(res.getInt("customerid")));
            PreparedStatement stmt2= this.con.prepareStatement("SELECT * FROM orders WHERE cartid=?");
            stmt2.setInt(1,res.getInt("id"));
            ResultSet res2= stmt2.executeQuery();
            ArrayList<Object[]> products= new ArrayList<Object[]>();
            while(res2.next()){
                System.out.println("adding");
                products.add(new Object[] {getProduct(res2.getInt("productid")),res2.getInt("quantity")});
            }
            order.add(products);
        }
        return order;
    }
}
