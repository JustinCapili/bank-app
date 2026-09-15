package utilities;

import java.util.*;

import com.example.simplebank.models.*;

public class AllData {
   public static List<User> users = new ArrayList<>();
   public static List<Account> accounts = new ArrayList();
   
   static {

   	User test = new User(1,"blue","123");
      test.createAccount("SAVING");
      test.createAccount("CHECKING");
      users.add(test);

	   test = new User(2,"green","124");
      test.createAccount("SAVING");
      test.createAccount("CHECKING");
      users.add(test);

	   test = new User(3,"red","125");
      test.createAccount("SAVING");
      test.createAccount("CHECKING");
      users.add(test);
   }
}
