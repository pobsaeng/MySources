package client;

import dao.AddressDAO;
import java.util.List;
import model.Address;

public class Demo {

    public static void main(String[] args) throws Exception {
        AddressDAO ad = new AddressDAO();        
          //Criteria
//        List<Address> list = ad.findAllAddress();
//        for (Address a : list) {
//            System.out.println("****************begin****************");
//            System.out.println(a.getId());
//            System.out.println(a.getZipcode());
//            System.out.println(a.getHomeno());
//            System.out.println(a.getRoad());
//            System.out.println(a.getProvince());
//            System.out.println("****************end******************");
//        }
        //annotation
        List<Address> list = ad.findAllNativeSQL();
        for (Address a : list) {
            System.out.println("****************begin****************");
            System.out.println(a.getId());
            System.out.println(a.getZipcode());
            System.out.println(a.getHomeno());
            System.out.println(a.getRoad());
            System.out.println(a.getProvince());
            System.out.println("****************end******************");
        }
    }

}
