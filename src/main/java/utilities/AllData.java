package utilities;

import java.util.*;

import com.example.simplebank.models.*;

import java.math.BigDecimal;

public class AllData {
   public static List<User> users = new ArrayList<>();
   public static List<Account> accounts = new ArrayList();
   
   static {

   	User test = new User(1,"blue","123");
      Account testSavings = new SavingAccount(11,new BigDecimal(0));
      Account testChecking = new CheckingAccount(12,new BigDecimal(0));
      test.addAccount(testChecking);
      test.addAccount(testSavings);
      accounts.add(testSavings);
      accounts.add(testChecking);
      users.add(test);

	   test = new User(2,"green","124");
      testSavings = new SavingAccount(21,new BigDecimal(0));
      testChecking = new CheckingAccount(22,new BigDecimal(0));
      test.addAccount(testChecking);
      test.addAccount(testSavings);
      accounts.add(testSavings);
      accounts.add(testChecking);
      users.add(test);

	   test = new User(3,"red","125");
      testSavings = new SavingAccount(31,new BigDecimal(0));
      testChecking = new CheckingAccount(32,new BigDecimal(0));
      test.addAccount(testChecking);
      test.addAccount(testSavings);
      accounts.add(testSavings);
      accounts.add(testChecking);
      users.add(test);
   }
}
