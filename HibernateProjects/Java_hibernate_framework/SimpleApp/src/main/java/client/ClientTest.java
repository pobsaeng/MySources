package client;

import java.util.Date;
import util.HibernateUtil;
import java.util.List;
import java.util.Set;
import dao.AddressDAO;
import dao.CustomerDAO;
import dao.BookDAO;
import dao.BorrowDetailDAO;
import dao.ServiceDAO;
import model.Address;
import model.Book;
import model.BorrowDetail;
import model.Customer;
import model.CustomerVIP;
import model.Service;
import org.hibernate.Session;

public class ClientTest {

    public void setDataTables() {

        Service service1 = new Service("Email");
        Service service2 = new Service("Surface mail");

        Book book1 = new Book("Basic Java", "For the beginner");
        Book book2 = new Book("Hibernate Java", "OR Mapping");
        Book book3 = new Book("JBoss", "Java Application Server: Open Source");
        Session session = null;
        try {

            session = HibernateUtil.getSession();
            session.beginTransaction();
            ServiceDAO servicedao = new ServiceDAO();
            servicedao.insert(service1);
            servicedao.insert(service2);

            BookDAO bookdao = new BookDAO();
            bookdao.insert(book1);
            bookdao.insert(book2);
            bookdao.insert(book3);
            session.getTransaction().commit();

        } catch (Exception ex) {

            ex.printStackTrace();
            session.getTransaction().rollback();
            System.out.println("Can not add records into look up tables");

        } finally {

//            session.close();
        }
    }

    public static void main(String[] args) {

        ClientTest test = new ClientTest();
        test.setDataTables();
        test.addCustomer();

        test.addBorrowDetail();
        test.update();
        test.delete();
        test.display();

    }

    public void addCustomer() {

        System.out.println("############# Insert #############");
        Customer cust1 = new CustomerVIP("JACK", "Marlet", 15, "Always");
        Customer cust2 = new Customer("YAN", "BILLANA", 20);
        Customer cust3 = new Customer("Tamoki", "JAKATA", 35);
        Address addr1 = new Address("30-32", "Latphroa", "BKK", "10310");
        Address addr2 = new Address("40-50", "Pahonyothin", "BKK", "11111");
        Address addr3 = new Address("55-89", "Bangna", "BKK", "12345");
        Address addr4 = new Address("54-84", "Bangkapi", "BKK", "34322");
       

        Session session = null;
        try {

            session = HibernateUtil.getSession();
            session.beginTransaction();
            ServiceDAO servicedao = new ServiceDAO();
            cust1.getServices().add(servicedao.findByPK(1));
            cust1.getServices().add(servicedao.findByPK(2));
            cust2.getServices().add(servicedao.findByPK(2));
            cust3.getServices().add(servicedao.findByPK(1));
            CustomerDAO custdao = new CustomerDAO();
            custdao.insert(cust1);
            custdao.insert(cust2);
            custdao.insert(cust3);

            AddressDAO addressdao = new AddressDAO();
            addr1.setCustomer(cust1);
            addr2.setCustomer(cust1);
            addr3.setCustomer(cust2);
            addr4.setCustomer(cust3);
            addressdao.insert(addr1);
            addressdao.insert(addr2);
            addressdao.insert(addr3);
            addressdao.insert(addr4);
            session.getTransaction().commit();

        } catch (Exception ex) {

            ex.printStackTrace();
            session.getTransaction().rollback();
            System.out.println("Can not add records into customer,add,service tables");

        } finally {

//            session.close();
        }

    }

    public void addBorrowDetail() {

        Date borrowdate = new Date(System.currentTimeMillis());
        Date duedate = new Date(System.currentTimeMillis() + 7 * 86400000);

        Session session = null;
        try {

            session = HibernateUtil.getSession();
            session.beginTransaction();
            CustomerDAO custdao = new CustomerDAO();
            Customer cust1 = custdao.findByPK(1);
            Customer cust2 = custdao.findByPK(2);
            Customer cust3 = custdao.findByPK(3);
            BookDAO bookdao = new BookDAO();
            Book book1 = bookdao.findByPK(1);
            Book book2 = bookdao.findByPK(2);
            BorrowDetail bookdetail1
                    = new BorrowDetail(cust1, null, borrowdate, duedate, book1);
            BorrowDetail bookdetail2
                    = new BorrowDetail(cust1, null, borrowdate, duedate, book2);
            BorrowDetail bookdetail3
                    = new BorrowDetail(cust2, null, borrowdate, duedate, book1);
            BorrowDetail bookdetail4
                    = new BorrowDetail(cust3, null, borrowdate, duedate, book2);

            BorrowDetailDAO borrowdetaildao = new BorrowDetailDAO();
            borrowdetaildao.insert(bookdetail1);
            borrowdetaildao.insert(bookdetail2);
            borrowdetaildao.insert(bookdetail3);
            borrowdetaildao.insert(bookdetail4);
            session.getTransaction().commit();

        } catch (Exception ex) {

            ex.printStackTrace();
            session.getTransaction().rollback();
            System.out.println("Can not insert data into BorrowDetail table");

        } finally {

//            session.close();
        }
    }

    public void display() {
        Session session = null;
        try {

            session = HibernateUtil.getSession();
            session.beginTransaction();
            displayBook();
            displayService();
            displayTransaction();
            session.getTransaction().commit();
        } catch (Exception ex) {

            ex.printStackTrace();
            session.getTransaction().rollback();
            System.out.println("Can not display Data");

        } finally {

//            session.close();
        }
    }

    public void displayBook() throws Exception {

        System.out.println("----------------------------------Table: Book"
                + "-------------------------------------");
        BookDAO bookdao = new BookDAO();
        List<Book> booklist = bookdao.findAll();
        if (booklist != null) {
            java.util.Iterator<Book> bookiter = booklist.iterator();
            while (bookiter.hasNext()) {
                Book book = (Book) bookiter.next();
                System.out.println("BookId: " + book.getId() + ", "
                        + "BookTitle: " + book.getTitle()
                        + ", Descipriton: " + book.getDescription());
            }
        } else {

            System.out.println("There is no data in Book Table");

        }

    }

    public void displayService() throws Exception {
        System.out.println("-----------------------------------Table"
                + ": Service----------------------------------");
        ServiceDAO servicedao = new ServiceDAO();
        List<Service> servicelist = servicedao.findAll();
        if (servicelist != null) {
            java.util.Iterator<Service> serviceiter = servicelist.iterator();
            while (serviceiter.hasNext()) {
                Service service = (Service) serviceiter.next();
                System.out.println("Service: " + service.getId() + ", "
                        + "Description: " + service.getDes());
            }
        } else {

            System.out.println("There is no data in Service Table");

        }
    }

    public void displayTransaction() throws Exception {

        System.out.println("---------------------Table: "
                + "Customer/Service/Address/BorrowDeatil-------------------");
        CustomerDAO custdao = new CustomerDAO();
        List<Customer> custlist = custdao.findAll();

        if (custlist != null) {

            java.util.Iterator<Customer> custiter = custlist.iterator();
            int no = 1;

            while (custiter.hasNext()) {

                Customer cust = (Customer) custiter.next();
                System.out.println("-----------------------------------Customer"
                        + no + ":---------------------------------------");
                no++;
                System.out.println("CustomerId: " + cust.getId()
                        + ", CustomerName: " + cust.getFirstname() + " "
                        + cust.getSurname() + ", Age: " + cust.getAge());

                Set<Service> services = cust.getServices();
                if (services != null) {
                    java.util.Iterator<Service> serviceiter
                            = services.iterator();
                    while (serviceiter.hasNext()) {
                        Service service = (Service) serviceiter.next();
                        System.out.println("ServiceDescription: "
                                + service.getDes());
                    }
                } else {
                    System.out.println("Customer has not registered "
                            + "any services");
                }

                AddressDAO addressdao = new AddressDAO();
                List<Address> addrlist = addressdao.findByCustId(cust.getId());
                if (addrlist != null) {
                    java.util.Iterator<Address> serviceiter
                            = addrlist.iterator();
                    while (serviceiter.hasNext()) {
                        Address address = (Address) serviceiter.next();
                        System.out.println("AddressId: "
                                + address.getId() + ", " + "Province: "
                                + address.getProvince()
                                + ",Road: " + address.getRoad()
                                + ",ZipCode: " + address.getZipcode());
                    }
                } else {

                    System.out.println("There is no address info for"
                            + "this customer");
                }

                BorrowDetailDAO borrowdetaildao = new BorrowDetailDAO();
                List<BorrowDetail> borrowdetaillist
                        = borrowdetaildao.findByCustId((int) cust.getId());
                if (borrowdetaillist != null) {
                    java.util.Iterator<BorrowDetail> borrowdetailiter
                            = borrowdetaillist.iterator();

                    while (borrowdetailiter.hasNext()) {
                        BorrowDetail borrowdetail
                                = (BorrowDetail) borrowdetailiter.next();
                        System.out.println("BookName : "
                                + borrowdetail.getBook().getTitle()
                                + ", DueDate: " + borrowdetail.getDuedate());
                    }
                } else {

                    System.out.println("There is no address info for"
                            + " this customer");

                }

            }// while close

        } else { // check if custlist!=null
            System.out.println("There is no data in Customer Table");
        }// close if

    }

    public void update() {

        CustomerDAO custdao = new CustomerDAO();
        AddressDAO addrdao = new AddressDAO();
        ServiceDAO servicedao = new ServiceDAO();
        BookDAO bookdao = new BookDAO();

        Session session = null;
        try {

            session = HibernateUtil.getSession();
            session.beginTransaction();

            Service service = servicedao.findByPK(1);
            service.setDes("CHANGE");

            Book book = bookdao.findByPK(1);
            book.setTitle("CHANGE");

            Address addr = addrdao.findByPK(1);
            addr.setZipcode("CHANGE");

            Customer cust = custdao.findByPK(1);
            cust.setAge(33);
            cust.setFirstname("CHANGE");

            custdao.update(cust);
            addrdao.update(addr);

            session.getTransaction().commit();

        } catch (Exception ex) {

            ex.printStackTrace();
            session.getTransaction().rollback();
            System.out.println("Can not update Data");

        } finally {

//            session.close();
        }
    }

    public void delete() {

        CustomerDAO custdao = new CustomerDAO();

        Session session = null;
        try {

            session = HibernateUtil.getSession();
            session.beginTransaction();
            Customer cust = custdao.findByPK(1);
            custdao.delete(cust);
            cust = custdao.findByPK(2);
            custdao.delete(cust);
            cust = custdao.findByPK(3);
            custdao.delete(cust);
            session.getTransaction().commit();

        } catch (Exception ex) {

            ex.printStackTrace();
            session.getTransaction().rollback();
            System.out.println("Can not delete Data");

        } finally {

//            session.close();
        }
    }

}
