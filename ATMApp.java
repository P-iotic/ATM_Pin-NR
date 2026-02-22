import java.util.Scanner;

public class ATMApp 
{
    //change these
    private static final String CORRECT_PIN = "1234";   //store as String to allow zeros
    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_DURATION_MS = 30_000; //30secs
    //ATM state
    private static double balance = 500.00; //starting balance
    public static void main(String[] args) 
    {
        Scanner scan = new Scanner(System.in);
        int tries = MAX_ATTEMPTS;//chances left
        long close = 0L;//lock until

        System.out.println("Welcome to Simple ATM");

        while (true) 
        {
            //check lock status
            long now = System.currentTimeMillis();
            if (now < close) 
            {
                long remainingMs = close - now;//remaining time
                long remainingSeconds = (remainingMs + 999) / 1000; //round up
                System.out.println("Account locked. Try again in " + remainingSeconds + "s.");
                sleep(1000); //wait for 1 second before showing again
                continue;
            }
            //if attempts are used up, lock and reset attempts
            if (tries == 0) 
            {
                close = System.currentTimeMillis() + LOCK_DURATION_MS;
                tries = MAX_ATTEMPTS;
                System.out.println("Too many wrong attempts. Please try again after 30 seconds.");
                continue;
            }
            //prompt PIN
            System.out.print("Enter PIN(or type EXIT): ");
            String pin = scan.nextLine().trim();
            if (pin.equalsIgnoreCase("EXIT")) 
            {
                System.out.println("Goodbye.");
                break;
            }
            //validate PIN format (integers only, length 4)
            if (!isValidPin(pin)) 
            {
                System.out.println("Invalid entry. PIN must be 4 digits.");
                continue;
            }
            //check PIN accuracy
            if (pin.equals(CORRECT_PIN)) 
            {
                System.out.println("PIN accepted.");
                runMenu(scan);
                //after user exits menu, end program
                System.out.println("Session over. Goodbye.");
                break;
            } 
            else 
            {
                tries--;
                System.out.println("Wrong PIN. Attempts left: " + tries);
            }
        }
        scan.close();
    }
    private static boolean isValidPin(String pin) 
    {
        if (pin.length() != 4) 
            return false;
        for (int i = 0; i < pin.length(); i++) 
        {
            char c = pin.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }
    private static void runMenu(Scanner scan) 
    {
        while (true) 
        {
            System.out.println("\nATM MENU");
            System.out.println("1 Check Balance");
            System.out.println("2 Deposit");
            System.out.println("3 Withdraw");
            System.out.println("4 Exit");
            System.out.print("Choose option: ");
            String choice = scan.nextLine().trim();
            switch (choice) 
            {
                case "1":
                    System.out.printf("Balance: R%.2f%n", balance);
                    break;
                case "2":
                    System.out.print("Enter deposit amount: ");
                    double dep = readAmount(scan);
                    if (dep > 0) 
                    {
                        balance += dep;
                        System.out.printf("Deposited R%.2f. New balance: R%.2f%n", dep, balance);
                    }
                    break;
                case "3":
                    System.out.print("Enter withdraw amount: ");
                    double amount = readAmount(scan);
                    if (amount > 0) 
                    {
                        if (amount > balance) 
                        {
                            System.out.println("Insufficient funds.");
                        }
                        else 
                        {
                            balance -= amount;
                            System.out.printf("Withdrew R%.2f. New balance: R%.2f%n", amount, balance);
                        }
                    }
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option. Choose 1-4.");
            }
        }
    }
    private static double readAmount(Scanner scan) 
    {
        String input = scan.nextLine().trim();
        //basic numeric validation 
        try 
        {
            double amount = Double.parseDouble(input);
            if (amount <= 0) 
            {
                System.out.println("Amount must be greater than 0.");
                return -1;
            }
            return amount;
        } 
        catch (NumberFormatException e) 
        {
            System.out.println("Invalid number.");
            return -1;
        }
    }
    private static void sleep(long ms) 
    {
        try 
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) 
        {
            Thread.currentThread().interrupt();
        }
    }
}