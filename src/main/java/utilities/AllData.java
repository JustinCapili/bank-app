package utilities;

import java.util.*;

import com.example.simplebank.models.*;

public class AllData {
   public static List<Account> accs = new ArrayList<>();
   
   static {
   	accs.add(new Account(1, 3.0));
	   accs.add(new Account(2, 4.0));
	   accs.add(new Account(3, 5.0));
   }
}
