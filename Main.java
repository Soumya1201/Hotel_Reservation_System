package HotelPackage;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        final String url = "jdbc:mysql://localhost:3306/hotel_db";
        final String username = "root";
        final String password = "SoumyaA@2004";

        try
        {
            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(url, username, password);
            Scanner sc = new Scanner(System.in);
            while(true)
            {
                System.out.println();
                System.out.println("HOTEL RESERVATION SYSTEM");
                System.out.println("1.New Reservation");
                System.out.println("2.Check Reservation");
                System.out.println("3.Room number");
                System.out.println("4.Update Reservation");
                System.out.println("5.Delete Reservation");
                System.out.println("6.Exit");

                System.out.println("Choose one option: ");
                int choice = sc.nextInt();
                switch(choice)
                {
                    case 1:
                        newReservation(conn, sc);
                        break;
                    case 2:
                        checkReservation(conn, sc);
                        break;
                    case 3:
                        getRoomNo(conn, sc);
                        break;
                    case 4:
                        updateReservation(conn, sc);
                        break;
                    case 5:
                        deleteReservation(conn, sc);
                        break;
                    case 6:
                        exitMenu();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice! Try again");
                }
            }
        }
        catch( SQLException| ClassNotFoundException |InterruptedException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void newReservation(Connection conn, Scanner sc)
    {
        System.out.print("Person name: ");
        String person_name = sc.next();
        sc.nextLine();
        System.out.print("Room no: ");
        int room_no = sc.nextInt();
//        System.out.println();
        System.out.print("Contact no: ");
        String contact = sc.next();
        System.out.println();


        String query = "insert into reservation(guest_name, room_no, contact_no) " +
                "values ('" + person_name + "', " + room_no + ", '" + contact + "');";
        try
        {
            Statement st = conn.createStatement();
            int rowAffected = st.executeUpdate(query);

            if(rowAffected > 0)
            {
                System.out.println("Reservation successful");
            }
            else
            {
                System.out.println("Reservation failed");
            }

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void checkReservation(Connection conn, Scanner sc)
    {
        String query = "select reservation_id, guest_name, room_no, contact_no, reservation_date from reservation;";
        try
        {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            System.out.println("---------------*------------*---------*------------*-------------------");
            System.out.println("Reservation id | Guest Name | Room no | Contact no | Reservation Date |");
            System.out.println("---------------*------------*---------*------------*-------------------");

            while(rs.next())
            {
                int id = rs.getInt("reservation_id");
                String name = rs.getString("guest_name");
                int room = rs.getInt("room_no");
                String contact = rs.getString("contact_no");
                String date = rs.getString("reservation_date");


                System.out.printf("| %-12d | %-10s | %-8d | %-10s | %-12s |\n",id,name,room,contact,date);
            }
            System.out.println("---------------*------------*---------*------------*-------------------");

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void getRoomNo(Connection conn, Scanner sc)
    {
        System.out.println("Enter ID: ");
        int id = sc.nextInt();
        System.out.println("Enter Guest name: ");
        String name = sc.next();
//        sc.nextLine();

        String query = "select room_no from reservation where reservation_id = " + id + " AND guest_name = '"
                + name + "';";
        try
        {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            if(rs.next())
            {
                int room = rs.getInt("room_no");
                System.out.printf("Room no for Reservation ID %d and Guest Name %s is %d: ",id, name, room);
            }
            else
            {
                System.out.println("Reservation not found for following Reservation ID and guest name.");
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void updateReservation(Connection conn, Scanner sc)
    {
        System.out.println("Enter Reservation ID: ");
        int id = sc.nextInt();
        if(reservationExists(conn, id))
        {
            System.out.println("Reservation not found for the given ID.");
            return;
        }
        System.out.println("Enter new Name: ");
        String name = sc.next();
        sc.nextLine();
        System.out.println("Enter new room number: ");
        int room = sc.nextInt();
        System.out.println("Enter new contact number: ");
        String contact = sc.next();

        String query = "update reservation set guest_name = '" + name + "', room_no = " + room
                + ", contact_no = '" + contact + "' where reservation_id = " + id + ";";

        try
        {
            Statement st = conn.createStatement();
            int rowAffected = st.executeUpdate(query);

            if(rowAffected > 0)
            {
                System.out.println("Updated successfully");
            }
            else
            {
                System.out.println("Update failed");
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteReservation(Connection conn, Scanner sc)
    {
        System.out.println("Enter the Reservation ID: ");
        int id = sc.nextInt();
        if(reservationExists(conn, id))
        {
            System.out.println("Reservation not found for the given ID.");
            return;
        }
        String query = "delete from reservation where reservation_id = " + id + ";";

        try
        {
            Statement st = conn.createStatement();

            int rowAffected = st.executeUpdate(query);

            if(rowAffected > 0)
            {
                System.out.println("Deletion successful");
            }
            else
            {
                System.out.println("Deletion failed");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static boolean reservationExists(Connection conn, int id)
    {
        String query = "select reservation_id from reservation where reservation_id = " + id + ";";
        try
        {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            return !rs.next();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return true;
        }
    }

    public static void exitMenu() throws InterruptedException {
        int i = 5;
        System.out.print("Exit");
        while (i != 0)
        {
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
    }
}
