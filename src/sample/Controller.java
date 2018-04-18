package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URI;
import java.util.*;


public class Controller {

    public static ObservableList<Guest> guests = FXCollections.observableArrayList();
    public static ObservableList<Item> items = FXCollections.observableArrayList();

    public static HashMap<Item,Integer> ownerSetup = new HashMap<>();

    private Guest selectedGuest;
    private Item selectedItem;

    public Controller() {
    }



    //
    // Menu Bar
    //

    @FXML
    MenuItem saveData;

    @FXML
    MenuItem loadData;

    @FXML
    MenuItem documentation;

    @FXML
    MenuItem saveAndExit;

    @FXML
    Button addItem;

    @FXML
    TextField removeItemNum;

    @FXML
    TextField removeGuestNum;

    @FXML
    Button addGuest;

    @FXML
    Button removeGuest;

    @FXML
    Button removeItem;


    //
    // Items
    //

    @FXML
    TextField itemName;

    @FXML
    TextField itemPrice;

    @FXML
    ComboBox<Item> itemSelect;

    @FXML
    TextArea itemNotes;

    @FXML
    TextField itemNumber;

    @FXML
    TextField itemOwner;

    @FXML
    ComboBox<Guest> ownerSelect;

    //
    // Guests
    //

    @FXML
    TextField lastName;

    @FXML
    TextField phoneNumber;

    @FXML
    TextField firstName;

    @FXML
    TextField email;

    @FXML
    TextField guestNumber;

    @FXML
    Label paymentNeeded;

    @FXML
    Label changeNeeded;

    @FXML
    TextField tShirt;

    @FXML
    TextField glasses;

    @FXML
    Label totalDue;

    @FXML
    TextField guestDonation;

    @FXML
    TextArea guestItemList;

    @FXML
    ComboBox<Guest> guestSelect;

    @FXML
    TextField changeGiven;

    @FXML
    TextField entryDonation;

    @FXML
    TextArea guestNotes;

    @FXML
    TextField amountPaid;

    @FXML
    CheckBox orderComplete;

    @FXML
    CheckBox auctionPaidByCheck;

    @FXML
    CheckBox entryPaidByCheck;

    @FXML
    public void saveData() {
        DataManager.saveData();
    }

    @FXML
    public void loadData() {
        DataManager.loadData();
        /*for(Item i : itemToAddOwnerTo)
        {
            Guest g = Guest.getGuestFromID(""+ownerToAdd.remove());
            if (g == null) continue;
            i.setOwner(g);
        }*/

        for (Item i:items) {
            if (ownerSetup.containsKey(i)) {
                Guest g = Guest.getGuestFromID(""+ownerSetup.get(i));
                if (g == null) continue;
                i.setOwner(g);
                ownerSetup.remove(i);
            }
        }

        guestSelect.setItems(guests);
        itemSelect.setItems(items);
        updateItem();
        updateGuest();
    }

    @FXML
    public void loadDocumentation() throws Exception{ //Loads Github Page with Documentation
        java.awt.Desktop.getDesktop().browse(new URI("https://github.com/Senarii/CheckoutEWB/blob/master/readme.md"));
    }

    @FXML
    public void saveAndExit() {
        saveData();
        System.exit(0);
    }

    @FXML
    public void newGuest() {
        Guest g = new Guest();
        g.setFirstName("[New Guest]");
        guests.add(g);
        FXCollections.sort(guests);
        guestSelect.setItems(guests);
        guestSelect.setValue(g);
        updateGuestTextField(g);
    }

    @FXML
    public void newItem() {
        Item i = new Item();
        i.setName("[New Item]");
        items.add(i);
        FXCollections.sort(items);
        itemSelect.setItems(items);
        itemSelect.setValue(i);
        updateItemTextField(i);
    }

    @FXML
    public void removeGuest() {
        if (removeGuestNum.getText() == null) return;
        Guest g = null;
        try {
            int guestNumber = Integer.parseInt(removeGuestNum.getText());
            g = Guest.getGuestFromID(""+guestNumber);
        } catch (Exception ignored) {}
        if (g == null) return;
        removeGuestNum.clear();
        guests.remove(g);
        g.remove();
        if (selectedGuest == g) {
            selectedGuest = null;
        }
            updateGuest();
            guestSelect.setItems(guests);

    }

    public void removeItem() {
        if (removeItemNum.getText() == null) return;
        Item i = null;
        try {
            int itemNumber = Integer.parseInt(removeItemNum.getText());
            i = Item.getItemFromID(""+itemNumber);
        } catch (Exception ignored) {}
        if (i == null) return;
        items.remove(i);
        i.remove();
        removeItemNum.clear();
        if (selectedItem == i) selectedItem = null;
        updateItem();
        itemSelect.setItems(items);
    }

    @FXML
    public void updateGuest() {
        if (selectedGuest == null) {clearGuestData(); return;}
        saveCurrentGuestData(selectedGuest);
        guestSelect.setItems(guests);
        updateGuestTextField(selectedGuest);
    }

    private void clearGuestData() {
        lastName.setText("");
        firstName.setText("");
        phoneNumber.setText("");
        email.setText("");
        tShirt.setText("");
        glasses.setText("");
        guestDonation.setText("");
        changeGiven.setText("");
        entryDonation.setText("");
        guestNotes.setText("");
        amountPaid.setText("");
        orderComplete.setSelected(false);
        auctionPaidByCheck.setSelected(false);
        entryPaidByCheck.setSelected(false);
        guestItemList.setText("");
        guestNumber.setText("");
        paymentNeeded.setText("");
        totalDue.setFont(Font.font("Verdana", FontWeight.BOLD,12));
        totalDue.setText("");
    }

    private void clearItemData() {
        itemName.setText("");
        itemPrice.setText("");
        itemOwner.setText("");
        itemNotes.setText("");
        itemNumber.setText("");
    }

    @FXML
    public void updateItem() {
        if (selectedItem==null) {clearItemData(); return;}
        saveCurrentItemData(selectedItem);
        itemSelect.setItems(items);
        updateItemTextField(selectedItem);
    }

    @FXML
    private void saveCurrentItemData(Item i) {
        i.setName(itemName.getText());
        i.setNotes(itemNotes.getText());

        try {
            int ownerNumber = Integer.parseInt(itemOwner.getText());
            Guest g = Guest.getGuestFromID(""+ownerNumber);
            i.setOwner(g);
        } catch (Exception ignored) {}

        try {
            double d = Double.parseDouble(itemPrice.getText());
            i.setPrice(d);
        } catch (Exception ignored) {}

        setItemNumber(); //Do this last

    }

    @FXML
    public void selectItemFromList() {
        Item i = itemSelect.getValue();
        selectedItem = i;
        updateItemTextField(i);
    }

    @FXML
    private void updateItemTextField(Item i) {
        if (i == null) {clearItemData(); return;}
        itemName.setText(i.getName());
        itemPrice.setText(""+i.getPrice());
        itemNumber.setText(""+i.getNumber());
        if (selectedItem.getOwner() != null) itemOwner.setText(""+i.getOwner().getNumber());
        else itemOwner.setText("");
        itemNotes.setText(i.getNotes());
    }

    @FXML
    public void setGuestNumber() {
        if (selectedGuest == null) return;
        int i = -1;
        try {
            i = Integer.parseInt(guestNumber.getText());
        } catch (Exception ignored) {}
        if (i < 0) return;
        selectedGuest.setNumber(i);
        FXCollections.sort(guests);
        guestSelect.setItems(guests);
        if (selectedItem != null && selectedItem.getOwner() == selectedGuest) itemOwner.setText(""+selectedItem.getOwner().getNumber());
    }

    @FXML
    public void setItemNumber() {
        if (selectedItem == null) return;
        int i = -1;
        try {
            i = Integer.parseInt(itemNumber.getText());
        } catch (Exception ignored) {}
        if (i < 0) return;
        selectedItem.setNumber(i);
        FXCollections.sort(items);
        itemSelect.setItems(items);
    }


    @FXML
    public void selectGuestFromList() {
        Guest g = guestSelect.getValue();
        selectedGuest = g;
        updateGuestTextField(g);
    }

    @FXML
    private void saveCurrentGuestData(Guest g) {
        //Direct String Inputs
        g.setLastName(lastName.getText());
        g.setFirstName(firstName.getText());
        g.setPhoneNumber(phoneNumber.getText());
        g.setEmail(email.getText());
        g.setNotes(guestNotes.getText());

        //Numerial Inputs
        try {
            int i = Integer.parseInt(tShirt.getText());
            g.setNumberShirts(i);
        } catch (Exception ignored) {}

        try {
            int i = Integer.parseInt(glasses.getText());
            g.setNumberCups(i);
        } catch (Exception ignored) {}

        try {
            double d = Double.parseDouble(guestDonation.getText());
            g.setDonation(d);
        } catch (Exception ignored) {}

        try {
            double d = Double.parseDouble(changeGiven.getText());
            g.setChangeGiven(d);
        } catch (Exception ignored) {}

        try {
            double d = Double.parseDouble(entryDonation.getText());
            g.setEntryDonation(d);
        } catch (Exception ignored) {}

        try {
            double d = Double.parseDouble(amountPaid.getText());
            g.setAmountPaid(d);
        } catch (Exception ignored) {}

        //Boolean Inputs
        g.setOrderComplete(orderComplete.isSelected());
        g.setPaidAuctionItemsCash(!auctionPaidByCheck.isSelected());
        g.setPaidEntryDonationCash(!entryPaidByCheck.isSelected());
        setGuestNumber();//Do This Last
    }

    @FXML
    private void updateGuestTextField(Guest g) {
        if (g == null) {clearGuestData(); return;}
        lastName.setText(g.getLastName());
        firstName.setText(g.getFirstName());
        phoneNumber.setText(g.getPhoneNumber());
        email.setText(g.getEmail());
        tShirt.setText(""+g.getNumberShirts());
        glasses.setText(""+g.getNumberCups());
        guestDonation.setText(""+g.getDonation());
        changeGiven.setText(""+g.getChangeGiven());
        entryDonation.setText(""+g.getEntryDonation());
        guestNotes.setText(g.getNotes());
        amountPaid.setText(""+g.getAmountPaid());
        orderComplete.setSelected(g.getOrderComplete());
        auctionPaidByCheck.setSelected(!g.getPaidAuctionItemsCash());
        entryPaidByCheck.setSelected(!g.getPaidEntryDonationCash());
        guestNumber.setText(""+g.getNumber());
        updateGuestItems(g);
        totalDue.setFont(Font.font("Verdana", FontWeight.BOLD,12));
        totalDue.setText(""+g.checkout());
        updatePrice();
        getChangeNeeded();
    }

    @FXML
    public void updatePrice() {
        if (selectedGuest==null) return;

        try {
            int i = Integer.parseInt(entryDonation.getText());
            selectedGuest.setEntryDonation(i);
        } catch (Exception ignored) {}

        try {
            int i = Integer.parseInt(tShirt.getText());
            selectedGuest.setNumberShirts(i);
        } catch (Exception ignored) {}

        try {
            int i = Integer.parseInt(glasses.getText());
            selectedGuest.setNumberCups(i);
        } catch (Exception ignored) {}

        try {
            double d = Double.parseDouble(guestDonation.getText());
            selectedGuest.setDonation(d);
        } catch (Exception ignored) {}

        try {
            double d = Double.parseDouble(amountPaid.getText());
            selectedGuest.setAmountPaid(d);
        } catch (Exception ignored) {}

        totalDue.setFont(Font.font("Verdana", FontWeight.BOLD,12));
        totalDue.setText(""+selectedGuest.checkout());

        if(selectedGuest.getAmountPaid() < selectedGuest.checkout()) {
            paymentNeeded.setFont(Font.font("Verdana", FontWeight.BOLD,12));
            paymentNeeded.setTextFill(Color.RED);
            paymentNeeded.setText("*Payment Required*");
        } else paymentNeeded.setText("");

        getChangeNeeded();

    }

    @FXML
    public void getChangeNeeded() {
        if (selectedGuest == null) return;
        try {
            double d = Double.parseDouble(amountPaid.getText());
            selectedGuest.setAmountPaid(d);
        } catch (Exception ignored) {}

        try {
            double d = Double.parseDouble(changeGiven.getText());
            selectedGuest.setChangeGiven(d);
        } catch (Exception ignored) {}

        double checkout = selectedGuest.checkout();
        double amountChange = selectedGuest.getAmountPaid()-checkout-selectedGuest.getChangeGiven();
        if(checkout > 0) {
            if ((selectedGuest.getAmountPaid() <= checkout && selectedGuest.getChangeGiven() > 0) || (amountChange < 0 && selectedGuest.getAmountPaid() >= checkout)) {
                changeNeeded.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                changeNeeded.setTextFill(Color.DARKRED);
                changeNeeded.setText("*ERROR IN PAYMENT*");

            } else if (amountChange > 0) {
                changeNeeded.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                changeNeeded.setTextFill(Color.GREEN);
                changeNeeded.setText("*Change Needed: $" + amountChange + "*");
            } else changeNeeded.setText("");
        } else changeNeeded.setText("");

    }


    @FXML
    public void removeItemOwner() {
        if(selectedItem==null) return;
        selectedItem.setOwner(null);
        itemOwner.clear();
    }

    private void updateGuestItems(Guest g) {
        StringBuilder owned = new StringBuilder();
        g.getItems().clear();
        for (Item i : items) {
            if (i.getOwner() == g) {
            owned.append("[$").append(i.getPrice()).append("]  #[").append(i.getNumber()).append("]    ").append(i.getName()).append("\n");
            g.getItems().add(i);
            }
        }
        guestItemList.setText(owned.toString());
    }



}
