import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {

    private static final String url="jdbc:mysql://localhost:3306/hotel_db";
    private static final String username="root";
    private static final String password="YOUR_PASSWORD";

    public static void main(String[] args) throws ClassNotFoundException,SQLException{
        try{
            Class.forName("com.mysql.mj.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection connection= DriverManager.getConnection(url, username, password);
            Statement statement= connection.createStatement();
            Scanner scanner= new Scanner(System.in);
            while(true){
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                System.out.println("--------------------------------------------------------");
                switch(choice){
                    case 1:
                        reserveRoom(scanner,statement);
                        break;
                    case 2:
                        viewReservations(connection,statement);
                        break;
                    case 3:
                        getRoomNumber(statement,scanner);
                        break;
                    case 4:
                        updateReservation(statement,scanner);
                        break;
                    case 5:
                        deleteReservation(statement,scanner);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        catch(InterruptedException e){
            throw new RuntimeException(e);
        }

    }
    private static void reserveRoom(Scanner scanner, Statement statement) {
        try{
            System.out.print("Enter guest name: ");
            String guestName=scanner.next();
            scanner.nextLine();
            System.out.print("Enter room number: ");
            int roomNumber=scanner.nextInt();
            System.out.print("Enter contact number: ");
            String contactNumber=scanner.next();

            String query="Insert into reservations (Guest_Name,Room_Number,Contact_Number)"+
                    "values('"+guestName+"',"+roomNumber+",'"+contactNumber+"')";

            try{
                int rowAffected= statement.executeUpdate(query);
                if(rowAffected>0){
                    System.out.println("--------------------------------------------------------");
                    System.out.println("Reservation Successful!");
                    System.out.println("--------------------------------------------------------");
                }
                else{
                    System.out.println("--------------------------------------------------------");
                    System.out.println("Reservation Failed.");
                    System.out.println("--------------------------------------------------------");
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
        catch(SQLException e){
            e.printStackTrace();


        }

    }

    private static void viewReservations (Connection connection, Statement statement) throws SQLException {
        String query="Select * from reservations";
        try(ResultSet resultSet=statement.executeQuery(query)){
            while(resultSet.next()){
                int reservation_id=resultSet.getInt("Reservation_Id");
                String guestName=resultSet.getString("Guest_Name");
                int roomNumber=resultSet.getInt("Room_Number");
                String contactNumber=resultSet.getString("Contact_Number");
                String reservationDate=resultSet.getTimestamp("Reservation_Date").toString();
                System.out.printf("%d|%s|%d|%s|%s|\n",reservation_id,guestName,roomNumber,contactNumber,reservationDate);
            }
            System.out.println("--------------------------------------------------------");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void getRoomNumber(Statement statement,Scanner scanner) {
        try{
            System.out.print("Enter reservation ID: ");
            int reservationId=scanner.nextInt();
            System.out.print("Enter guest name: ");
            String guestName=scanner.next();

            String query="Select Room_Number from reservations "+
                    "where Reservation_Id= "+ reservationId +
                    " And Guest_Name = '"+ guestName + "'";

            try(ResultSet resultSet = statement.executeQuery(query)){
                if(resultSet.next()){
                    int roomNumber=resultSet.getInt("Room_Number");
                    System.out.println("--------------------------------------------------------");
                    System.out.println("Room number for Reservation Id "+reservationId+
                            " And guest "+guestName+" is: "+roomNumber);
                    System.out.println("--------------------------------------------------------");
                }
                else{
                    System.out.println("--------------------------------------------------------");
                    System.out.println("Reservation not found for the given id and guest name.");
                    System.out.println("--------------------------------------------------------");
                }

            }
            }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void updateReservation(Statement statement,Scanner scanner){
        try{
            System.out.print("Enter reservation ID to update: ");
            int reservationId=scanner.nextInt();
            if(!reservationExists(statement,reservationId)){
                System.out.println("--------------------------------------------------------");
                System.out.println("Reservation not found for the given ID.");
                System.out.println("--------------------------------------------------------");
                return;
            }

            System.out.print("Enter new Guest name: ");
            String guestName=scanner.next();
            System.out.print("Enter new room number: ");
            int roomNumber=scanner.nextInt();
            System.out.print("Enter new contact number: ");
            String contactNumber=scanner.next();

            String query="Update reservations SET Guest_Name='"+guestName+"',"+
                    "Room_Number='"+roomNumber+"',"+
                    "Contact_Number='"+contactNumber+"' " +
                    "where reservation_Id="+reservationId;
            try{
                int rowaffected=statement.executeUpdate(query);
                if(rowaffected>0){
                    System.out.println("--------------------------------------------------------");
                    System.out.println("Reservation updated Successfully!");
                    System.out.println("--------------------------------------------------------");
                }
                else{
                    System.out.println("--------------------------------------------------------");
                    System.out.println("Reservation update failed!");
                    System.out.println("--------------------------------------------------------");
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Statement statement,Scanner scanner){
        try{
            System.out.print("Enter reservation ID to delete: ");
            int reservationId=scanner.nextInt();

            if(!reservationExists(statement,reservationId)){
                System.out.println("--------------------------------------------------------");
                System.out.println("Reservation not found for the given ID.");
                System.out.println("--------------------------------------------------------");
                return;
            }
            String query="DELETE FROM reservations where reservation_id="+reservationId;

            try{
                int rowaffected=statement.executeUpdate(query);
                if(rowaffected>0){
                    System.out.println("--------------------------------------------------------");
                    System.out.println("Reservation Deleted Successfully");
                    System.out.println("--------------------------------------------------------");
                }
                else{
                    System.out.println("--------------------------------------------------------");
                    System.out.println("Reservation deletion failed!");
                    System.out.println("--------------------------------------------------------");
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }

            }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Statement statement,int reservationId){
        String query="Select reservation_id from reservations where reservation_id="+reservationId;
        try(ResultSet resultSet=statement.executeQuery(query)){
            return resultSet.next();

        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }


    }

    public static void exit() throws InterruptedException{
        System.out.print("Existing System");
        int i=5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou For Using Hotel Reservation System!!");
        System.out.println("--------------------------------------------------------");
    }

}
