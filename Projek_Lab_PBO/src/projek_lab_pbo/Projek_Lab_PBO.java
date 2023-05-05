/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
*/
package projek_lab_pbo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 *
 * @author LENOVO
 */
public class Projek_Lab_PBO {

    /**
     * @param args the command line arguments
     */
    private static ArrayList<BankAccount> accounts = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        boolean keluar_program = false;
        while (!keluar_program) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    Daftar_Akun_Baru();
                    break;
                case 2:
                    Kirim_Uang();
                    break;
                case 3:
                    Simpan_Uang();
                    break;
                case 4:
                    keluar_program = true;
                    System.out.println("\nTerima Kasih dan Sampai Jumpa");
                    break;
                default:
                    System.out.println("KESALAHAN : Silahkan Pilih Menu Antara 1 Hingga 4");
                    break;
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n||===============================================||");
        System.out.println("||---------------  Aplikasi Bank  ---------------||");
        System.out.println("||------------ 1. Daftar Akun Bank  -------------||");
        System.out.println("||------- 2. Kirim Uang ke Akun Bank Lain  ------||");
        System.out.println("||-------------- 3. Deposit Uang  ---------------||");
        System.out.println("||------------ 4. Keluar dari Menu  -------------||");
        System.out.println("||===============================================||");
        System.out.print("Masukkan Pilihan : ");
    }

    private static void Daftar_Akun_Baru() {
        System.out.println("\n||------------ 1. Daftar Akun Bank  -------------||");
        
        String name;
    try {
        System.out.print("Masukkan Nama Anda : ");
        name = scanner.nextLine();
        if (!name.matches("^[a-zA-Z ]+$")) {
            throw new InputMismatchException();
        }
    } catch (InputMismatchException e) {
        System.out.println("KESALAHAN : Masukkan Nama Anda dalam Huruf dan Spasi");
        return;
    }
    
        String accountNumber;
        boolean isUnique = false;
        do {
            accountNumber = generateAccountNumber();
            if (isAccountNumberUnique(accountNumber)) {
                isUnique = true;
            }
        } while (!isUnique);
        
        double balance;
        try {
            System.out.print("Masukkan Saldo Awal : ");
            balance = scanner.nextDouble();
            scanner.nextLine(); // consume newline character
        } catch (InputMismatchException e) {
            System.out.println("KESALAHAN : Masukkan Saldo Awal dalam Angka");
            scanner.nextLine(); // consume invalid input
            return;
        }
        
        LocalDateTime registrationDate = LocalDateTime.now();
        BankAccount account = new BankAccount(name, accountNumber, balance, registrationDate);
        accounts.add(account);

        System.out.println("Akun Bank Berhasil Didaftarkan");
        System.out.println("Nomor Akun Bank : " + accountNumber);
        System.out.println("Tanggal Pendaftaran Akun : " + formatDateTime(registrationDate));
        System.out.println("Saldo Akun Anda : " + balance);
    }

    private static void Kirim_Uang() {
        System.out.println("||------- 2. Kirim Uang ke Akun Bank Lain  ------||");
        System.out.print("Masukkan Nomor Akun Pengirim : ");
        String senderAccountNumber = scanner.nextLine();

        System.out.print("Masukkan Nomor Akun Penerima : ");
        String recipientAccountNumber = scanner.nextLine();
    
         double amount;
        try {
            System.out.print("Masukkan Jumlah Uang yang akan Dikirim : ");
            amount = scanner.nextDouble();
            scanner.nextLine(); // consume newline character
        } catch (InputMismatchException e) {
            System.out.println("KESALAHAN : Masukkan Jumlah Uang dalam Angka");
            scanner.nextLine(); // consume invalid input
            return;
        }

        BankAccount senderAccount = getAccountByAccountNumber(senderAccountNumber);
        if (senderAccount == null) {
            System.out.println("KESALAHAN : Nomor Akun Pengirim Tidak Sesuai");
            return;
        }

        BankAccount recipientAccount = getAccountByAccountNumber(recipientAccountNumber);
        if (recipientAccount == null) {
            System.out.println("KESALAHAN : Nomor Akun Penerima Tidak Sesuai");
            return;
        }

        if (senderAccount.getBalance() < amount) {
            System.out.println("KESALAHAN : Saldo Tidak Mencukupi");
            return;
        }

        senderAccount.withdraw(amount);
        recipientAccount.deposit(amount);

        System.out.println("Transaksi Berhasil");
        System.out.println("Sisa Saldo Pengirim : " + senderAccount.getBalance());
        System.out.println("Sisa Saldo Penerima : " + recipientAccount.getBalance());
    }

    private static void Simpan_Uang() {
        System.out.println("||-------------- 3. Deposit Uang  ---------------||");
        System.out.print("Masukkan Nomor Akun Bank : ");
        String accountNumber = scanner.nextLine();

        System.out.print("Masukkan Jumlah Uang : ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline character

        if (!isAmountValid(amount)) {
            System.out.println("KESALAHAN : Masukkan Jumlah Uang yang Sesuai");
            return;
        }

        BankAccount account = getAccountByAccountNumber(accountNumber);
        if (account == null) {
            System.out.println("KESALAHAN : Akun Bank tidak Sesuai");
            return;
        }

        account.deposit(amount);

        System.out.println("Deposit Berhasil");
        System.out.println("Nominal Saldo : " + account.getBalance());
    }

    private static String generateAccountNumber() {
        int accountNumber;
        do {
            accountNumber = (int) (Math.random() * 1000000);
        } while (String.valueOf(accountNumber).length() != 6);
        return String.valueOf(accountNumber);
    }

    private static boolean isAccountNumberUnique(String accountNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAmountValid(double amount) {
        return amount >= 0;
    }

    private static BankAccount getAccountByAccountNumber(String accountNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    private static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Y-M-d H:m:s");
        return dateTime.format(formatter);
    }
}

class BankAccount {
    private String name;
    private String accountNumber;
    private double balance;
    private LocalDateTime registrationDate;

    public BankAccount(String name, String accountNumber, double balance, LocalDateTime registrationDate) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.registrationDate = registrationDate;
    }

    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }   
}