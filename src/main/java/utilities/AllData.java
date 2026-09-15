package utilities;

import java.util.*;

import com.example.simplebank.models.*;

public class AllData {
   public static List<Account> accs = new ArrayList<>();
   
   static {
   	accs.add(new CheckingAccount(100, 3.0));
	   accs.add(new SavingAccount(2, 4.0));
	   accs.add(new CheckingAccount(3, 5.0));
   }
}
