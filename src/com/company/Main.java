package com.company;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to Cave Escape! This is a text-based adventure game, so you need to type in commands to " +
                "make perform an action.");
        System.out.println("Remember that with every action you take you will lose some energy, so be sure to eat food along the way." +
                "\nIf energy is too low or is depleted, you will start to lose health.");
        System.out.println("If you wish to view a list of possible commands at any time, enter in the command <instruction>");
        System.out.println("And now, the game begins....\n\n");
        System.out.println("You wake up in a dimly lit room, with nothing but a torch. There are four ways out of this " +
                "room, the north entrance, south entrance, east entrance and west entrance.\nSo, enter in the desired direction you would like to go.");


        Player p = new Player();

        Scanner input = new Scanner(System.in);

        boolean i = true;

        while( p.getHealth() > 0 ){

            System.out.print("\nEnter your direction or command: ");
            String direction = input.nextLine();

            if(direction.toLowerCase().equals("exit")){
                System.out.println("Thank you for playing Cave Escape!");
                i = false;
            } else if(direction.toLowerCase().equals("show")){
                p.printStack();
            } else{

                Room temp;
                temp = p.getCurrentRoom();
                Item weapon = p.getCurrentTool();

                int length = direction.length();

                if(length>=11 && direction.substring(0,11).equals("instruction")){
                    System.out.println("The commands are:" +
                            "\nattack: this command allows you to attack an animal" +
                            "\ncollect <enter corresponding number>: this command allows you to collect an item in a room" +
                            "\ndeploy <enter animal name>: this command allows you to use a pet to attack another animal" +
                            "\ndrink <name of drink>: this command allows you to consume items that are in a liquid state" +
                            "\ndrop <enter name of item>: this command allows you to drop an item from your inventory" +
                            "\neat <enter food name>: this command allows you to eat a food item" +
                            "\neast: this command allows you to travel through the east path of a room" +
                            "\nexit: this command exits the game" +
                            "\nexplore: this command shows you all the items a room contains" +
                            "\nforge <enter tool name>: this command allows you to forge tools and weapons" +
                            "\nhint: this command shows a hint for the room you are currently in" +
                            "\ninventory: this command shows you all the items you currently have" +
                            "\nnorth: this command allows you to travel through the north path of a room" +
                            "\nshow: this command shows you which rooms you have already visited" +
                            "\nsouth: this command allows you to travel through the south path of a room" +
                            "\nstats: this command shows you your current health, energy and carrying capacity" +
                            "\ntame: this command allows you to tame an animal" +
                            "\ntoggle: this command allows you to switch between tools" +
                            "\nunlock: this command allows you to unlock a lock if you have the correct key" +
                            "\nwest: this command allows you to travel through the west path of a room"
                    );
                } else if(length>=6 && direction.substring(0,6).toLowerCase().equals("deploy")){
                    String animalName = direction.substring(7);
                    Animal animal = p.searchAnimal(animalName);

                    if(temp.getAnimal()==null){
                        System.out.println("There are no animals in this room to attack.");
                    } else{
                        int damage = animal.attack();
                        // System.out.println(damage + " " + weapon.display());
                        if( damage >= temp.getAnimal().hp()){
                            temp.getAnimal().takeDamage(damage);
                            System.out.println("Your attack was successful! You have killed the " + temp.getAnimal().getName() + "!");
                            System.out.println( "Your pet has a remaining hp of " + animal.hp());
                        } else{
                            animal.takeDamage(temp.getAnimal().attack());
                            System.out.println(temp.getAnimal().getName() + " has attacked your pet back! Your pet has a remaining hp of " + animal.hp());
                            if(p.getHealth()<=0){
                                System.out.println("Your pet was defeated :(");
                                p.getPetsList().remove(animal);
                            } else{
                                temp.getAnimal().takeDamage(damage);
                                System.out.println( temp.getAnimal().getName() + " is not dead yet. It has a remaining hp of " + temp.getAnimal().hp());
                            }
                        }
                    }

                } else if(length>=3 && direction.substring(0,3).toLowerCase().equals("pet")){
                    p.showPets();
                } else if(length>=3 && direction.substring(0,3).toLowerCase().equals("map")){
                    Item map = p.searchItem("Map");
                    if(map==null){
                        System.out.println("This command is invalid because you do not have a map. Proceed to the Map Room to find a map.");
                    } else{
                        Map realMap = new Map();
                        System.out.println(realMap.readMap());
                    }
                } else if(length>=5 && direction.substring(0,5).equals("stats")){
                    System.out.println("Health: " + p.getHealth());
                    System.out.println("Energy: " + p.getEnergy());
                    System.out.println("Capacity: " + p.getCapacity());
                } else if( length>=5 && direction.substring(0,5).toLowerCase().equals("forge")){
                    // Item f = p.searchItem(direction.substring(4));
                    temp.forge(direction.substring(6),p.getInventory());
                    Item armor = p.searchItem("Armor");
                    if(armor != null){
                        p.incHelath();
                    }
                }
                else if(length>=4 && direction.toLowerCase().equals("tame")){
                    if( (length>=4 && temp.roomName().equals("Dinosaur Room") || temp.roomName().equals("Snake Room")))
                        if( p.searchItem("Meat")!=null){
                            temp.getAnimal().setTame(true);
                            p.tameAnimal(temp.getAnimal());
                            p.getInventory().remove(p.searchItem("Meat"));
                            System.out.println("You have successfully tamed " + temp.getAnimal().getName());
                        } else{
                            System.out.println("You need to acquire meat first in order to tame animals.");
                        }
                    else{
                        System.out.println("No animals here can be tamed.");
                    }
                } else if( length>=3 && direction.substring(0,3).toLowerCase().equals("eat")){
                    // Item f = p.searchItem(direction.substring(4));
                    p.eat(direction.substring(4));
                } else if( length>=5 && direction.substring(0,5).toLowerCase().equals("drink")){
                    // Item f = p.searchItem(direction.substring(6));
                    p.drink(direction.substring(6));
                } else if( length>=6 && direction.toLowerCase().equals("unlock") && (temp.roomName().equals("Checkpoint Corridor"))){
                    Item k = p.searchItem(temp.roomName());
                    // p.reduceEnergy(1);
                    if(k!=null){
                        System.out.println("You have unlocked the lock!");
                        temp.getLock().openLock();
                    } else{
                        System.out.println("You do not have the key to open this lock.");
                    }
                } else if( length>=4 && direction.substring(0,4).toLowerCase().equals("drop")){
                    String item = direction.substring(5);
                    // Item obj = temp.getItem(item);
                    Item obj = p.searchItem(item.toLowerCase());
                    if(obj == null){
                        System.out.println("This item was not found in your inventory.");
                    } else{
                        p.dropItem(item);
                        p.incCapacity(obj.storage());
                    }
                } else if( length>=6 && direction.toLowerCase().equals("attack") && weapon!=null){
                    if(temp.getAnimal()==null){
                        System.out.println("There are no animals in this room to attack.");
                    } else{
                        p.reduceEnergy(3);
                        int damage = weapon.getDamage();
                        if(damage == 0){
                            System.out.println("The attack strength of this weapon was 0. Use the toggle command to switch to a more power weapon.");
                        }
                        // System.out.println(damage + " " + weapon.display());
                        if( damage >= temp.getAnimal().hp()){
                            temp.getAnimal().takeDamage(damage);
                            System.out.println("Your attack was successful! You have killed the " + temp.getAnimal().getName() + "!");
                            System.out.println( "You have a remaining hp of " + p.getHealth());

                            if(temp.getAnimal().getName().equals("Reptile King")){
                                System.out.println("You have won to game! Congratulations! Thank you for playing Cave Escape.");
                                return;
                            }
                        } else{
                            p.takeDamage(temp.getAnimal().attack());
                            System.out.println(temp.getAnimal().getName() + " has attacked you back! You have a remaining hp of " + p.getHealth());
                            if(p.getHealth()<=0){
                                System.out.println("You died! Game Over :(");
                                return ;
                            } else{
                                temp.getAnimal().takeDamage(damage);
                                System.out.println( temp.getAnimal().getName() + " is not dead yet. It has a remaining hp of " + temp.getAnimal().hp());
                            }
                        }
                    }
                } else if( length>=6 && direction.toLowerCase().equals("toggle") && weapon!=null){
                    weapon = p.changeTool();
                } else if( length>=9 && direction.toLowerCase().equals("inventory")){
                    p.showInventory();
                } else if( length>=7 && direction.toLowerCase().equals("explore")){
                    p.reduceEnergy(2);
                    temp.showItem();
                } else if( length>=4 && direction.toLowerCase().equals("hint")){
                    temp.showHint();
                }else if(  (direction.toLowerCase().equals("north") || direction.toLowerCase().equals("south") || direction.toLowerCase().equals("east") || direction.toLowerCase().equals("west"))) {
                    temp = temp.input(direction);
                    p.reduceEnergy(2);
                    if(temp != null){
                        if(p.searchPath(temp.roomName())!=null){
                            temp = p.searchPath(temp.roomName());
                        }
                        p.pushRoom(temp);
                        p.setCurrentRoom(temp);
                        // p.printStack();
                    }else{
                        System.out.println("Entrance is blocked. Try a different direction.");
                    }
                } else if( direction.length() >= 7 && direction.substring(0,7).toLowerCase().equals("collect") ){
                    // p.reduceEnergy(1);
                    String item = direction.substring(8);
                    int index = Integer.parseInt(item);
                    if(index > temp.roomItems().size()-1){
                        System.out.println("Please type in a number that corresponds to a resource.");
                    } else{

                        String roomName = temp.roomName();
                        if(roomName.equals("Mammoth Room") && temp.getAnimal().hp()>0){
                            System.out.println("You must defeat the Mammoth to collect its meat and tusks.");
                        } else if(roomName.equals("Snake Room") && !temp.getAnimal().isTame()){
                            System.out.println("You must first tame the snake in order to get this item.");
                        } else if(roomName.equals("Mysterious Lake Room") && p.searchItem("Venom")==null ){
                            System.out.println("You need to collect Venom from Sanke Room in order to acquire this item.");
                        } else{
                            if(p.getCapacity() <= 0){
                                System.out.println("Your capacity limit has been reached. Drop unwanted items or consume food items to free up space.");
                            } else{
                                Item obj = temp.getItem(item);
                                p.pickUpItem(obj);
                                p.showInventory();
                                p.decCapacity(obj.storage());
                                if(obj.display().equals("Armor")){
                                    p.incHelath();
                                }
                            }

                        }

                    }

                }
                else{
                    System.out.println("Invalid command. Try Again.");
                }

            }

        }



    }
}

//Room classes
abstract class Room implements Hint{
    String north = "North";
    String south = "South";
    String east = "East";
    String west = "West";

    abstract Room input(String input);
    abstract String roomName();
    abstract void showItem();
    abstract Item getItem(String i);
    abstract ArrayList<Item> roomItems();
    abstract Animal getAnimal();
    abstract Lock getLock();
    abstract void forge(String name, ArrayList<Item> supply);
}

class SpawnRoom extends Room {
    Room sRoom;
    Room nRoom;
    Room eRoom;
    Room wRoom;



    ArrayList<Item> items = new ArrayList<>();


    public SpawnRoom(){

    }

    public Room input(String input) {
        String userIn = input.toLowerCase();

        if(userIn.equals("south")){
            System.out.println("You are now in Map Room.");
            sRoom = new MapRoom();
            return sRoom;
        }
        else if(userIn.equals("north")){
            System.out.println("You are now in Small Animal Room.");
            nRoom = new SmallAnimalRoom();
            return nRoom;
        }
        else if(userIn.equals("east")){
            System.out.println("You are now in Utility Room.");
            eRoom = new UtilityRoom();
            return eRoom;
        }
        else if(userIn.equals("west")){
            System.out.println("You are now in Mysterious Lake Room.");
            wRoom = new MysteriousLakeRoom();
            return wRoom;
        }
        return null;
    }

    public void showItem(){
        System.out.println("Resources in this room:");

        if( !(items.isEmpty()) ){
            for(int x = 0; x< items.size(); x++){
                System.out.println("[" + x + "] " + items.get(x).display());
            }
        } else{
            System.out.println("Nothing");
        }

    }

    public String roomName(){
        return "Spawn Room";
    }

    public void showHint(){
        System.out.println("Hint:\nThis is a small and mostly empty room where the player starts. Explore the area, there are other rooms you can go to.");
    }

    public ArrayList<Item> roomItems(){
        return items;
    }

    @Override
    Animal getAnimal() {
        return null;
    }

    @Override
    Lock getLock() {
        return null;
    }

    @Override
    void forge(String name, ArrayList<Item> supply) {
        System.out.println("Forging is not available in this room.");
    }


    public Item getItem( String i ){
        int index = Integer.parseInt(i);
        Item temp =  items.get(index);
        items.remove(index);
        return temp;
    }
}

class MapRoom extends Room {

    Map map = new Map();
    Room nRoom;

    ArrayList<Item> items = new ArrayList<>();

    public MapRoom(){
        items.add(map);
    }

    Room input(String input) {
        String userIn = input.toLowerCase();

        if(userIn.equals("north")){
            System.out.println("You are now in Spawn Room.");
            nRoom = new SpawnRoom();
            return nRoom;
        }
        return null;
    }

    public String roomName(){
        return "Map Room";
    }

    public void showItem(){
        System.out.println("Resources in this room:");

        if( !(items.isEmpty()) ){
            for(int x = 0; x< items.size(); x++){
                System.out.println("[" + x + "] " + items.get(x).display());
            }
        } else{
            System.out.println("Nothing");
        }

    }

    public Item getItem( String i ){
        int index = Integer.parseInt(i);
        Item temp =  items.get(index);
        items.remove(index);
        System.out.println("You have now acquired map. Type in the command <map> in order to view its contents.");
        return temp;
    }

    ArrayList<Item> roomItems() {
        return items;
    }

    @Override
    Animal getAnimal() {
        return null;
    }

    @Override
    Lock getLock() {
        return null;
    }

    @Override
    void forge(String name, ArrayList<Item> supply) {
        System.out.println("Forging is not available in this room.");
    }

    public void showHint(){
        System.out.println("Hint:\nThis room has objects useful for navigating the cave system.");
    }
}

class UtilityRoom extends Room {
    Bola bola = new Bola();
    Fruit fruit = new Fruit();
    Spear spear = new Spear();
    Room nRoom;
    Room wRoom;



    ArrayList<Item> items = new ArrayList<>();

    public UtilityRoom(){
        items.add(bola);
        items.add(fruit);
        items.add(spear);
    }

    Room input(String input) {
        String userIn = input.toLowerCase();

        if(userIn.equals("north")){
            System.out.println("You are now in Clearing Room.");
            nRoom = new ClearingRoom();
            return nRoom;
        }
        else if(userIn.equals("west")){
            System.out.println("You are now in Spawn Room.");
            wRoom  = new SpawnRoom();
            return wRoom;
        }
        return null;
    }

    String roomName() {
        return "Utility Room";
    }


    public void showItem(){
        System.out.println("Resources in this room:");

        if( !(items.isEmpty()) ){
            for(int x = 0; x< items.size(); x++){
                if(items.get(x)!=null){
                    System.out.println("[" + x + "] " + items.get(x).display());
                }

            }
        } else{
            System.out.println("Nothing");
        }

    }

    ArrayList<Item> roomItems() {
        return items;
    }

    @Override
    Animal getAnimal() {
        return null;
    }

    @Override
    Lock getLock() {
        return null;
    }

    @Override
    void forge(String name, ArrayList<Item> supply) {
        System.out.println("Forging is not available in this room.");
    }

    public Item getItem( String i ){
        int index = Integer.parseInt(i);
        Item temp =  items.get(index);
        items.remove(index);
        return temp;
    }

    public void showHint(){
        System.out.println("Hint:\nThis room has objects useful for surviving the game. These items can prove to be of great use later on.");
    }
}

class MysteriousLakeRoom extends Room {
    int water = 100;
    Room eRoom;
    HealingPotion healing = new HealingPotion();

    Axe axe = new Axe();
    BowArrow bowArrow = new BowArrow();
    Hammer hammer = new Hammer();
    BasicSword basicSword = new BasicSword();

    ArrayList<Item> items = new ArrayList<>();

    public MysteriousLakeRoom(){
        items.add(healing);
    }

    Room input(String input) {
        String userIn = input.toLowerCase();

        if(userIn.equals("east")){
            System.out.println("You are now in Spawn Room.");
            eRoom  = new SpawnRoom();
            return eRoom;
        }
        return null;
    }
    String roomName() {
        return "Mysterious Lake Room";
    }

    ArrayList<Item> roomItems() {
        return items;
    }

    @Override
    Animal getAnimal() {
        return null;
    }

    @Override
    Lock getLock() {
        return null;
    }

    @Override
    void forge(String name, ArrayList<Item> supply) {

        int stone = 0;
        int wood = 0;
        ArrayList<Item> temp = new ArrayList<>();

        if( name.toLowerCase().equals("axe") || name.toLowerCase().equals("bow and arrow") || name.toLowerCase().equals("basic sword") || name.toLowerCase().equals("hammer")){

            if(name.toLowerCase().equals("axe")){
                for(Item m : supply){
                    if(m.display().equals("Stone") && stone<2){
                        stone++;
                        temp.add(m);
                    }
                    if(m.display().equals("Wood") && wood<2){
                        wood++;
                        temp.add(m);
                    }
                    if(stone == 2 && wood == 2){
                        System.out.println("You have now acquired the Axe!");
                        supply.remove(temp.get(0));
                        supply.remove(temp.get(1));
                        supply.remove(temp.get(2));
                        supply.remove(temp.get(3));
                        supply.add(axe);
                        temp.clear();
                        return;
                    }
                }
                System.out.println("You do not have the supplies to craft said tool");
            } else if(name.toLowerCase().equals("bow and arrow")){
                for(Item m : supply){
                    if(m.display().equals("Stone") && stone<1){
                        stone++;
                        temp.add(m);
                    }
                    if(m.display().equals("Wood") && wood<3){
                        wood++;
                        temp.add(m);
                    }
                    if(stone == 1 && wood == 3){
                        System.out.println("You have now acquired the Bow and Arrow!");
                        supply.remove(temp.get(0));
                        supply.remove(temp.get(1));
                        supply.remove(temp.get(2));
                        supply.remove(temp.get(3));
                        supply.add(bowArrow);
                        temp.clear();
                        return;
                    }
                }
                System.out.println("You do not have the supplies to craft said tool");
            } else if(name.toLowerCase().equals("basic sword")){
                for(Item m : supply){
                    if(m.display().equals("Stone") && stone<3){
                        stone++;
                        temp.add(m);
                    }
                    if(m.display().equals("Wood") && wood<1){
                        wood++;
                        temp.add(m);
                    }
                    if(stone == 3 && wood == 1){
                        System.out.println("You have now acquired the Basic Sword!");
                        supply.remove(temp.get(0));
                        supply.remove(temp.get(1));
                        supply.remove(temp.get(2));
                        supply.remove(temp.get(3));
                        supply.add(basicSword);
                        temp.clear();
                        return;
                    }
                }
                System.out.println("You do not have the supplies to craft said tool");
            } else if(name.toLowerCase().equals("hammer")){
                for(Item m : supply){
                    if(m.display().equals("Stone") && stone<1){
                        stone++;
                        temp.add(m);
                    }
                    if(m.display().equals("Wood") && wood<3){
                        wood++;
                        temp.add(m);
                    }
                    if(stone == 1 && wood == 3){
                        System.out.println("You have now acquired the Hammer!");
                        supply.remove(temp.get(0));
                        supply.remove(temp.get(1));
                        supply.remove(temp.get(2));
                        supply.remove(temp.get(3));
                        supply.add(hammer);
                        temp.clear();
                        return;
                    }
                }
                System.out.println("You do not have the supplies to craft said tool");
            }

        } else{
            System.out.println("Invalid tool name.\nYour options: Axe, Bow and Arrow, Basic Sword, and Hammer.");
        }

    }

    public void showItem(){
        System.out.println("Resources in this room:");

        if( !(items.isEmpty()) ){
            for(int x = 0; x< items.size(); x++){
                System.out.println("[" + x + "] " + items.get(x).display());
            }
        } else{
            System.out.println("Nothing");
        }
    }

    public Item getItem( String i ){
        int index = Integer.parseInt(i);
        Item temp =  items.get(index);
        items.remove(index);
        return temp;
    }

    public void showHint(){
        System.out.println("Hint:\nThis room has a lake with dark blue water; be careful.You can retrieve a healing potion from this room if you possess snake venom.\nAdditionally, you have the opportunity to forge weapons like Axe, Basic Sword, Hammer, Bow and Arrow, assuming you have the required resources for it.");
    }
}

class SmallAnimalRoom extends Room {
    SmallAnimal smallAnimal = new SmallAnimal();
    Room sRoom;
    Room wRoom;
    Meat meat = new Meat();

    ArrayList<Item> items = new ArrayList<>();

    public SmallAnimalRoom(){
        items.add(meat);
    }

    Animal getAnimal(){
        return smallAnimal;
    }

    @Override
    Lock getLock() {
        return null;
    }

    @Override
    void forge(String name, ArrayList<Item> supply) {
        System.out.println("Forging is not available in this room.");
    }

    Room input(String input) {
        String userIn = input.toLowerCase();

        if(userIn.equals("south")){
            System.out.println("You are now in Spawn Room.");
            sRoom = new SpawnRoom();
            return sRoom;
        }
        else if(userIn.equals("west")){
            System.out.println("You are now in Mineral Deposit Room.");
            wRoom = new MineralDepositRoom();
            return wRoom;
        }
        return null;
    }
    String roomName() {
        return "Small Animal Room";
    }

    ArrayList<Item> roomItems() {
        return items;
    }


    public void showItem(){
        System.out.println("Resources in this room:");

        if( !(items.isEmpty()) && smallAnimal.hp()<=0 ){
            for(int x = 0; x< items.size(); x++){
                System.out.println("[" + x + "] " + items.get(x).display());
            }
        } else{
            System.out.println("Nothing");
        }

        if(smallAnimal.hp() > 0){
            System.out.println("Animals in this room:\nSmall Animal");
        } else {
            System.out.println("You have defeated all the animals in this room!");
        }
    }

    public Item getItem( String i ){
        int index = Integer.parseInt(i);
        Item temp =  items.get(index);
        items.remove(index);
        return temp;
    }

    public void showHint(){
        System.out.println("Hint:\nThis room has nothing interesting other than a small animal, which you may choose to kill to acquire additional resources.");
    }
}

class MineralDepositRoom extends Room {
    MineralOre mineralOre = new MineralOre();
    ExoticBerry exoticBerry2 = new ExoticBerry();
    Room nRoom;
    Room eRoom;

    ArrayList<Item> items = new ArrayList<>();

    public MineralDepositRoom(){
        items.add(mineralOre);
        items.add(exoticBerry2);
    }

    Room input(String input) {
        String userIn = input.toLowerCase();

        if(userIn.equals("north")){
            System.out.println("You are now in Volcano Room.");
            nRoom = new VolcanoRoom();
            return nRoom;
        }
        else if(userIn.equals("south")){
            System.out.println("You are now in Small Animal Room.");
            eRoom = new SmallAnimalRoom();
            return eRoom;
        }
        return null;
    }

    String roomName() {
        return "Mineral Deposit Room";
    }

    ArrayList<Item> roomItems() {
        return items;
    }

    @Override
    Animal getAnimal() {
        return null;
    }

    @Override
    Lock getLock() {
        return null;
    }

    @Override
    void forge(String name, ArrayList<Item> supply) {
        System.out.println("Forging is not available in this room.");
    }

    public void showItem(){
        System.out.println("Resources in this room:");

        if( !(items.isEmpty()) ){
            for(int x = 0; x< items.size(); x++){
                System.out.println("[" + x + "] " + items.get(x).display());
            }
        } else{
            System.out.println("Nothing");
        }

    }

    public Item getItem( String i ){
        int index = Integer.parseInt(i);
        Item temp =  items.get(index);
        items.remove(index);
        return temp;
    }

    public void showHint(){
        System.out.println("Hint:\nThis room has a valuable resource; be sure to collect it. Resources found here has the potential to be transformed into something essential later on");
    }
}

class VolcanoRoom extends Room {

    Room sRoom;
    Room nRoom;
    Room eRoom;
    Gem gem = new Gem();

    ArrayList<Item> items = new ArrayList<>();

    public VolcanoRoom(){
        items.add(gem);
    }

    Room input(String input) {
        String userIn = input.toLowerCase();

        if(userIn.equals("south")){
            System.out.println("You are now in the Mineral Deposit Room.");
            sRoom = new MineralDepositRoom();
            return sRoom;
        }
        else if(userIn.equals("north")){
            System.out.println("You are now in the Armor Room.");
            nRoom = new ArmorRoom();
            return nRoom;
        } else if(userIn.equals("east")){
            System.out.println("You are now in the Dinosaur Room.");
            eRoom = new DinosaurRoom();
            return eRoom;
        }
        return null;
    }


    String roomName() {
        return "Volcano Room";
    }

    ArrayList<Item> roomItems() {
        return items;
    }

    @Override
    Animal getAnimal() {
        return null;
    }

    @Override
    Lock getLock() {
        return null;
    }

    @Override
    void forge(String name, ArrayList<Item> supply) {
        System.out.println("Forging is not available in this room.");
    }


    public void showItem(){
        System.out.println("Resources in this room:");

        if( !(items.isEmpty()) ){
            for(int x = 0; x< items.size(); x++){
                System.out.println("[" + x + "] " + items.get(x).display());
            }
        } else{
            System.out.println("Nothing");
        }

    }

    public Item getItem( String i ){
        int index = Integer.parseInt(i);
        Item temp =  items.get(index);
        items.remove(index);
        return temp;
    }

    public void showHint(){
        System.out.println("Hint:\nThis room is filled with lava; be careful where you step. Keep the gem with you, it will prove useful later on");
    }
}

class ArmorRoom extends Room {

    Room eRoom;
    Room sRoom;
    EliteSword sword = new EliteSword();
    MagicStaff staff = new MagicStaff();
    Armor armor = new Armor();
    boolean hasMaterial = false;

    ArrayList<Item> items = new ArrayList<>();


    public ArmorRoom(){
    }

    Room input(String input) {
        String userIn = input.toLowerCase();

        if(userIn.equals("east")){
            System.out.println("You have now reached the checkpoint corridor.");
            eRoom = new CheckPointCorridor();
            return eRoom;
        }
        else if(userIn.equals("south")){
            System.out.println("You are now in Volcano Room.");
            sRoom = new VolcanoRoom();
            return sRoom;
        }
        return null;
    }
    String roomName() {
        return "Armor Room";
    }

    ArrayList<Item> roomItems() {
        return items;
    }

    @Override
    Animal getAnimal() {
        return null;
    }

    @Override
    Lock getLock() {
        return null;
    }

    @Override
    void forge(String name, ArrayList<Item> supply) {

        Item a;
        Item b;

        if( name.equals("Elite Sword") || name.equals("Magic Staff") || name.equals("Armor") ){

            if(name.equals("Elite Sword")){
                for(Item m : supply){
                    if(m.display().equals("Reflective metal")){
                        a = m;
                        hasMaterial = true;
                        supply.remove(a);
                        supply.add(sword);
                        System.out.println("You have now acquired the Elite Sword!");
                        return;
                    }
                }
                System.out.println("You do not have the supplies to craft said tool.");
            } else if(name.equals("Magic Staff")){
                for(Item m : supply){
                    for(Item n : supply){
                        if(m.display().equals("Mammoth Tusks") && n.display().equals("Gem")){
                            supply.add(staff);
                            supply.remove(m);
                            supply.remove(n);
                            System.out.println("You have now acquired the Magic Staff!");
                            return;
                        }
                    }
                }
                System.out.println("You do not have the supplies to craft said tool.");
            } else if(name.equals("Armor")){
                for(Item m : supply){
                    if(m.display().equals("Mineral ore")){
                        supply.add(armor);
                        supply.remove(m);
                        System.out.println("You have now acquired the Armor!");

                        return;
                    }
                }
                System.out.println("You do not have the supplies to craft said tool.");
            }

        } else{
            System.out.println("Invalid tool name.\nYour options: Elite Sword, Magic Staff, Armor.");
        }

    }

    public void showItem(){
        System.out.println("Resources in this room:");

        if( !(items.isEmpty()) ){
            for(int x = 0; x< items.size(); x++){
                System.out.println("[" + x + "] " + items.get(x).display());
            }
        } else{
            System.out.println("Nothing");
        }

    }

    public Item getItem( String i ){
        int index = Integer.parseInt(i);
        Item temp =  items.get(index);
        items.remove(index);
        return temp;
    }

    // Elite Sword: reflective metal
    // Armor: mineral ore
    // Magic Staff: mammoth tusk, gem

    public void showHint(){
        System.out.println("Hint:\nThis room is very beneficial to prepare for the final room of the game. Make sure to utilize the forge command. You can forge armor, an elite sword, or a magic staff, but make sure you use the correct material");
        System.out.println("Required material:\nElite Sword: Reflective Metal\nArmor: Mineral Ore\nMagic Staff: Mammoth Tusk, Gem");
    }
}

class ClearingRoom extends Room {
    Fruit fruit2 = new Fruit();
    Wood wood1 = new Wood();
    Wood wood2 = new Wood();
    Wood wood3 = new Wood();
    Wood wood4 = new Wood();
    Wood wood5 = new Wood();
    Stone stone1 = new Stone();
    Stone stone2 = new Stone();
    Stone stone3 = new Stone();
    Stone stone4 = new Stone();
    Stone stone5 = new Stone();
    Room sRoom;
    Room nRoom;

    ArrayList<Item> items = new ArrayList<>();

    public ClearingRoom(){
        items.add(fruit2);
        items.add(wood1);
        items.add(wood2);
        items.add(wood3);
        items.add(wood4);
        items.add(wood5);
        items.add(stone1);
        items.add(stone2);
        items.add(stone3);
        items.add(stone4);
        items.add(stone5);
    }

    Room input(String input) {
        String userIn = input.toLowerCase();

        if(userIn.equals("south")){
            System.out.println("You are now in Utility Room.");
            sRoom = new UtilityRoom();
            return sRoom;
        }
        else if(userIn.equals("north")){
            System.out.println("You are now in Bone Room.");
            nRoom = new BoneRoom();
            return nRoom;
        }
        return null;
    }

    String roomName() {
        return "Clearing Room";
    }

    ArrayList<Item> roomItems() {
        return items;
    }

    @Override
    Animal getAnimal() {
        return null;
    }

    @Override
    Lock getLock() {
        return null;
    }

    @Override
    void forge(String name, ArrayList<Item> supply) {
        System.out.println("Forging is not available in this room.");
    }

    public void showItem(){
        System.out.println("Resources in this room:");

        if( !(items.isEmpty()) ){
            for(int x = 0; x< items.size(); x++){
                System.out.println("[" + x + "] " + items.get(x).display());
            }
        } else{
            System.out.println("Nothing");
        }

    }

    public Item getItem( String i ){
        int index = Integer.parseInt(i);
        Item temp =  items.get(index);
        items.remove(index);
        return temp;
    }

    public void showHint(){
        System.out.println("Hint:\nThis room is mostly open, and there are sticks, stones, and a fruit item lying on the floor. The stones and wood found here can be used to craft things, as long as you have the proper quantities");
    }
}

class MeteorRoom extends Room {
    ReflectiveMetal reflectiveMetal = new ReflectiveMetal();
    ExoticBerry berry = new ExoticBerry();

    Room eRoom;

    ArrayList<Item> items = new ArrayList<>();

    public MeteorRoom(){
        items.add(reflectiveMetal);
        items.add(berry);
    }

    Room input(String input) {
        String userIn = input.toLowerCase();

        if(userIn.equals("east")){
            System.out.println("You are now in Bone Room.");
            eRoom = new BoneRoom();
            return eRoom;
        }
        return null;
    }

    String roomName() {
        return "Meteor Room";
    }

    ArrayList<Item> roomItems() {
        return items;
    }

    @Override
    Animal getAnimal() {
        return null;
    }

    @Override
    Lock getLock() {
        return null;
    }

    @Override
    void forge(String name, ArrayList<Item> supply) {
        System.out.println("Forging is not available in this room.");
    }

    public void showItem(){
        System.out.println("Resources in this room:");

        if( !(items.isEmpty()) ){
            for(int x = 0; x< items.size(); x++){
                System.out.println("[" + x + "] " + items.get(x).display());
            }
        } else{
            System.out.println("Nothing");
        }

    }

    public Item getItem( String i ){
        int index = Integer.parseInt(i);
        Item temp =  items.get(index);
        items.remove(index);
        return temp;
    }

    public void showHint(){
        System.out.println("Hint:\nThis room has the remnants of a meteor. Keep the ore with you, it serves a greater purpose if used with something else");
    }
}

class BoneRoom extends Room {
    Key key = new Key(2468, "Checkpoint Corridor");
    Room sRoom;
    Room nRoom;
    Room wRoom;
    Room eRoom;

    ArrayList<Item> items = new ArrayList<>();

    public BoneRoom(){
        items.add(key);
    }

    Room input(String input) {
        String userIn = input.toLowerCase();

        if(userIn.equals("south")){
            System.out.println("You are now in Clearing Room.");
            sRoom = new ClearingRoom();
            return sRoom;
        }
        else if(userIn.equals("north")){
            System.out.println("You are now in Dinosaur Room.");
            nRoom = new DinosaurRoom();
            return nRoom;
        }
        else if(userIn.equals("west")){
            System.out.println("You are now in Meteor Room.");
            wRoom = new MeteorRoom();
            return wRoom;
        }
        else if(userIn.equals("east")){
            System.out.println("You are now in Mammoth Room.");
            eRoom = new MammothRoom();
            return eRoom;
        }
        return null;
    }

    String roomName() {
        return "Bone Room";
    }

    ArrayList<Item> roomItems() {
        return items;
    }

    @Override
    Animal getAnimal() {
        return null;
    }

    @Override
    Lock getLock() {
        return null;
    }

    @Override
    void forge(String name, ArrayList<Item> supply) {
        System.out.println("Forging is not available in this room.");
    }


    public void showItem(){
        System.out.println("Resources in this room:");

        if( !(items.isEmpty()) ){
            for(int x = 0; x< items.size(); x++){
                System.out.println("[" + x + "] " + items.get(x).display() + " Key");
            }
        } else{
            System.out.println("Nothing");
        }

    }

    public Item getItem( String i ){
        int index = Integer.parseInt(i);
        Item temp =  items.get(index);
        items.remove(index);
        return temp;
    }

    public void showHint(){
        System.out.println("Hint:\nThis room only contains a pile of bones; useless right? Search the room, there might be something useful hidden here");
    }
}

class MammothRoom extends Room {
    Mammoth mammoth = new Mammoth();
    Meat meat = new Meat();
    MammothTusks tusk = new MammothTusks();
    Room wRoom;
    Room nRoom;

    ArrayList<Item> items = new ArrayList<>();

    public MammothRoom(){
        items.add(meat);
        items.add(tusk);
    }

    Room input(String input) {
        String userIn = input.toLowerCase();

        if(userIn.equals("west")){
            System.out.println("You are now in Bone Room.");
            wRoom = new BoneRoom();
            return wRoom;
        }
        else if(userIn.equals("north")){
            System.out.println("You are now in Snake Room.");
            nRoom = new SnakeRoom();
            return nRoom;
        }
        return null;
    }

    String roomName() {
        return "Mammoth Room";
    }

    ArrayList<Item> roomItems() {
        return items;
    }

    @Override
    Animal getAnimal() {
        return mammoth;
    }

    @Override
    Lock getLock() {
        return null;
    }

    @Override
    void forge(String name, ArrayList<Item> supply) {
        System.out.println("Forging is not available in this room.");
    }

    public void showItem(){
        System.out.println("Resources in this room:");

        if(  !(items.isEmpty()) && mammoth.hp()<=0 ){
            for(int x = 0; x< items.size(); x++){
                System.out.println("[" + x + "] " + items.get(x).display());
            }
        } else{
            System.out.println("Nothing");
        }
        if(mammoth.hp() > 0){
            System.out.println("Animals in this room:\nMammoth");
        } else {
            System.out.println("You have defeated all the animals in this room!");
        }
    }

    public Item getItem( String i ){
        int index = Integer.parseInt(i);
        Item temp =  items.get(index);
        if( ( temp.display().equals("Meat") || temp.display().equals("Mammoth Tusks")) && mammoth.hp()<=0){
            items.remove(index);
            return temp;
        }
        return null;
    }

    public void showHint(){
        System.out.println("Hint:\nThis room contains a Mammoth which will not attack you if not provoked. With the right weapons the mammoth can be killed.");
    }
}

class DinosaurRoom extends Room {
    Dinosaur TRex = new Dinosaur();
    Room sRoom;
    Room wRoom;

    ArrayList<Item> items = new ArrayList<>();

    public DinosaurRoom(){

    }

    Room input(String input) {
        String userIn = input.toLowerCase();

        if(userIn.equals("south")){
            System.out.println("You are now in Bone Room.");
            sRoom = new BoneRoom();
            return sRoom;
        } else if(userIn.equals("west")){
            System.out.println("You are now in Volcano Room.");
            wRoom = new VolcanoRoom();
            return wRoom;
        }
        return null;
    }

    String roomName() {
        return "Dinosaur Room";
    }

    ArrayList<Item> roomItems() {
        return items;
    }

    @Override
    Animal getAnimal() {
        return TRex;
    }

    @Override
    Lock getLock() {
        return null;
    }

    @Override
    void forge(String name, ArrayList<Item> supply) {
        System.out.println("Forging is not available in this room.");
    }

    public void showItem(){
        System.out.println("Resources in this room:");

        if( !(items.isEmpty()) ){
            for(int x = 0; x< items.size(); x++){
                System.out.println("[" + x + "] " + items.get(x).display());
            }
        } else{
            System.out.println("Nothing");
        }
        if(TRex.hp() > 0){
            System.out.println("Animals in this room:\nT-Rex");
        } else {
            System.out.println("You have defeated all the animals in this room!");
        }


    }

    public Item getItem( String i ){
        int index = Integer.parseInt(i);
        Item temp =  items.get(index);
        items.remove(index);
        return temp;
    }

    public void showHint(){
        System.out.println("Hint:\nThis room contains a hungry T.Rex. Perhaps consider turning it into an asset somehow?");
    }
}

class SnakeRoom extends Room {
    Snake snake = new Snake();
    Venom venom = new Venom();
    Room sRoom;
    Room wRoom;

    ArrayList<Item> items = new ArrayList<>();

    public SnakeRoom(){
        items.add(venom);

    }

    Room input(String input) {
        String userIn = input.toLowerCase();

        if(userIn.equals("south")){
            System.out.println("You are now in Mammoth Room.");
            sRoom = new MammothRoom();
            return sRoom;
        }
        else if(userIn.equals("west")){
            System.out.println("You have now reached the checkpoint corridor.");
            wRoom = new CheckPointCorridor();
            return wRoom;
        }
        return null;
    }
    String roomName() {
        return "Snake Room";
    }

    ArrayList<Item> roomItems() {
        return items;
    }

    @Override
    Animal getAnimal() {
        return snake;
    }

    @Override
    Lock getLock() {
        return null;
    }

    @Override
    void forge(String name, ArrayList<Item> supply) {
        System.out.println("Forging is not available in this room.");
    }

    public void showItem(){
        System.out.println("Resources in this room:");

        if( !(items.isEmpty()) && snake.isTame() ){
            for(int x = 0; x< items.size(); x++){
                System.out.println("[" + x + "] " + items.get(x).display());
            }
        } else{
            System.out.println("Nothing");
        }

        if(snake.hp() > 0){
            System.out.println("Animals in this room:\nSnake");
        } else {
            System.out.println("You have defeated all the animals in this room!");
        }
    }

    public Item getItem( String i ){
        int index = Integer.parseInt(i);
        Item temp = items.get(index);
        items.remove(index);
        return temp;
    }

    public void showHint(){
        System.out.println("Hint:\nThis eery green room has nothing but a venomous serpent. If you give the snake what it wants, it might do something for you in return");
    }
}

class CheckPointCorridor extends Room {
    Room nRoom;
    Room eRoom;
    Room wRoom;
    Lock lock = new Lock(2468);

    ArrayList<Item> items = new ArrayList<>();

    public CheckPointCorridor(){
        items.add(lock);
    }

    Room input(String input) {
        String userIn = input.toLowerCase();

        if(userIn.equals("north")){
            if(lock.isOpen()){
                System.out.println("You have now reached the Boss.");
                nRoom = new BossRoom();
                return nRoom;
            } else{
                System.out.println("You have to unlock the door first.");
                return null;
            }
        }
        else if(userIn.equals("east")){
            System.out.println("You are now in Snake Room.");
            eRoom = new SnakeRoom();
            return eRoom;
        }
        else if(userIn.equals("west")){
            System.out.println("You are now in Armor Room.");
            wRoom = new ArmorRoom();
            return wRoom;
        }
        return null;
    }

    String roomName() {
        return "Checkpoint Corridor";
    }

    ArrayList<Item> roomItems() {
        return items;
    }

    @Override
    Animal getAnimal() {
        return null;
    }

    @Override
    Lock getLock() {
        return lock;
    }

    @Override
    void forge(String name, ArrayList<Item> supply) {
        System.out.println("Forging is not available in this room.");
    }

    public void showItem(){
        System.out.println("Resources in this room:");
        if( !(items.isEmpty()) ){
            for(int x = 0; x< items.size(); x++){
                System.out.println("[" + x + "] " + items.get(x).display());
            }
        } else{
            System.out.println("Nothing");
        }
    }

    public Item getItem( String i ){
        int index = Integer.parseInt(i);
        Item temp =  items.get(index);
        items.remove(index);
        return temp;
    }

    public void showHint(){
        System.out.println("Hint:\nThis is a passageway to the final room in the game, and it is guarded by a lock...");
    }
}


class BossRoom extends Room {
    Boss ReptileKing = new Boss();

    ArrayList<Item> items = new ArrayList<>();

    public BossRoom(){
    }

    Room input(String input) {
        return null;
    }

    String roomName() {
        return "Boss Room! This is the final room of the game, get past this and you're home free.";
    }

    ArrayList<Item> roomItems() {
        return items;
    }

    @Override
    Animal getAnimal() {
        return ReptileKing;
    }

    @Override
    Lock getLock() {
        return null;
    }

    @Override
    void forge(String name, ArrayList<Item> supply) {
        System.out.println("Forging is not available in this room.");
    }

    public void showItem(){
        System.out.println("Resources in this room:");

        if( !(items.isEmpty()) ){
            for(int x = 0; x< items.size(); x++){
                System.out.println("[" + x + "] " + items.get(x).display());
            }
        } else{
            System.out.println("Nothing");
        }
        System.out.println("Animals in this room:\nReptile King");
    }

    public Item getItem( String i ){
        int index = Integer.parseInt(i);
        Item temp =  items.get(index);
        items.remove(index);
        return temp;
    }

    public void showHint(){
        System.out.println("Hint:\nIt would be easier to defeat the Reptile King if you had some allies (like a friendly dinosaur or a trusty Snake). In order to get back to the T-Rex room" +
                " go south, east, south, west, and north. To get to the Snake room, go south and then east.");
    }
}

//Animal classes
abstract class Animal {

    public abstract int attack();
    public abstract int hp();
    public abstract void takeDamage(int damage);
    public abstract String getName();
    public abstract boolean isTame();
    public abstract void setTame(boolean b);
    //public abstract void eat();

}

class SmallAnimal extends Animal {

    int hp = 10;
    boolean tame = false;

    public int attack() {
        return 5;
    }

    public int hp(){
        return hp;
    }

    public void takeDamage(int damage){
        hp -= damage;
    }

    @Override
    public String getName() {
        return "Small Animal";
    }

    @Override
    public boolean isTame() {
        return tame;
    }

    @Override
    public void setTame(boolean b) {
        tame = b;
    }

}

class Mammoth extends Animal {

    int hp = 40;
    boolean tame = false;

    public int attack() {
        return 5;
    }

    public int hp(){
        return hp;
    }

    public void takeDamage(int damage){
        hp -= damage;
    }

    @Override
    public String getName() {
        return "Mammoth";
    }

    @Override
    public boolean isTame() {
        return tame;
    }

    @Override
    public void setTame(boolean b) {
        tame = b;
    }
}

class Dinosaur extends Animal {

    int hp = 70;
    boolean tame = false;

    public int attack() {
        return 25;
    }

    public int hp(){
        return hp;
    }

    public void takeDamage(int damage){
        hp -= damage;
    }

    @Override
    public String getName() {
        return "T-Rex";
    }

    @Override
    public boolean isTame() {
        return tame;
    }

    @Override
    public void setTame(boolean b) {
        tame = b;
    }
}

class Snake extends Animal {
    Venom venom;
    int hp = 30;
    boolean tame = false;

    public int attack() {
        return 15;
    }

    public int hp(){
        return hp;
    }

    public void takeDamage(int damage){
        hp -= damage;
    }

    @Override
    public String getName() {
        return "Snake";
    }

    @Override
    public boolean isTame() {
        return tame;
    }

    @Override
    public void setTame(boolean b) {
        tame = b;
    }
}

class Boss extends Animal {

    int hp = 100;
    boolean tame = false;

    public int attack(){
        int attack = (int) (Math.random()*25);
        if(attack == 0){
            System.out.println("You have withstood the attack.");
        } else{
            System.out.println("The Reptile King inflicted an attack of " + attack + ". Reptile King has an hp of " + hp + " remaining.");
        }
        return attack;
    }

    public int hp(){
        return hp;
    }

    public void takeDamage(int damage){
        hp -= damage;
    }

    @Override
    public String getName() {
        return "Reptile King";
    }

    @Override
    public boolean isTame() {
        return tame;
    }

    @Override
    public void setTame(boolean b) {
        tame = b;
    }
}

//Item classes
abstract class Item {

    //private int storage;
    public abstract int storage();
    public String display(){return "";}
    abstract int getDamage();
}




class Armor extends Item{

    int storage = 5;

    public String display(){ return "Armor"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 0;
    }

}


class Torch extends Item {
    int storage = 5;

    public String display(){ return "Torch"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 0;
    }
}


class Fruit extends Item {
    int storage = 5;

    public String display(){ return "Fruit"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 0;
    }
}

class ExoticBerry extends Item {
    int storage = 5;

    public String display(){ return "Exotic Berry"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 0;
    }
}

class Water extends Item {
    int storage = 5;

    public String display(){ return "Water"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 0;
    }
}

class HealingPotion extends Item {
    int storage = 5;

    public String display(){ return "Healing Potion"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 0;
    }
}


class Map extends Item {
    int storage = 5;

    public String display(){ return "Map"; }
    public int storage(){ return storage; }
    public String readMap(){
        String output = "";
        output += "To get back to Spawn Room, go north.\nFrom Spawn Room, go east to get to Utility Room" +
                " and then north from there to get to Clearing Room.\nFrom Spawn Room, go north to get to Small" +
                " Animal Room and then west from there to get to the Mineral Deposit Room.\n";
        output += "Remember that you can go back to other rooms to get the necessary resources.";
        return output;
    }

    int getDamage() {
        return 0;
    }
}

class MineralOre extends Item {
    int storage = 5;

    public String display(){ return "Mineral ore"; }
    public int storage(){ return storage; }
    public void changeForm(String newForm){
        System.out.println(newForm);
    }

    int getDamage() {
        return 0;
    }
}

class Gem extends Item {
    int storage = 5;

    public String display(){ return "Gem"; }
    public int storage(){ return storage; }

    int getDamage() {
        return 0;
    }
}

class ReflectiveMetal extends Item {
    int storage = 5;

    public String display(){ return "Reflective metal"; }
    public int storage(){ return storage; }
    public void changeForm(String newForm){
        System.out.println(newForm);
    }

    int getDamage() {
        return 0;
    }
}

class Wood extends Item {
    int storage = 1;

    public String display(){ return "Wood"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 0;
    }
}

class Stone extends Item {
    int storage = 1;

    public String display(){ return "Stone"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 0;
    }
}

class Key extends Item {
    int pin;
    int storage = 5;
    String name = "";

    public Key(int p, String name){
        pin = p;
        this.name = name;
    }

    public String display(){ return name; }
    public int storage(){ return storage; }
    int getDamage() {
        return 0;
    }

    int getPin(){
        return pin;
    }
}

class Lock extends Item {
    int pin;
    int storage = 5;
    boolean isOpen = false;

    public Lock(int p){
        pin = p;
    }
    public String display(){ return "Lock"; }
    public int storage(){ return storage; }

    public void openLock(){
        isOpen = true;
    }

    public boolean isOpen(){
        return isOpen;
    }

    public void isKey(Key k){
        if(k.getPin() == pin){
            System.out.println("The key was a match! You have unlocked the lock.");
            isOpen = true;
        }
        System.out.println("The key was not a match :(");
    }

    int getDamage() {
        return 0;
    }

}

class Meat extends Item {
    int storage = 5;

    public String display(){ return "Meat"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 0;
    }
}

class Venom extends Item {
    int storage = 5;

    public String display(){ return "Venom"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 0;
    }
}

class MammothTusks extends Item {
    int storage = 5;

    public String display(){ return "Mammoth Tusks"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 0;
    }
}

class Bola extends Item {
    int storage = 5;

    public String display(){ return "Bola"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 5;
    }
}

class Pickaxe extends Item {
    int storage = 5;

    public String display(){ return "Pickaxe"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 3;
    }
}

class Axe extends Item {
    int storage = 5;

    public String display(){ return "Axe"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 10;
    }
}

class Hammer extends Item {
    int storage = 5;

    public String display(){ return "Hammer"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 6;
    }
}

class BasicSword extends Item {
    int storage = 5;

    public String display(){ return "Basic Sword"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 10;
    }
}

class Spear extends Item {
    int storage = 5;

    public String display(){ return "Spear"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 7;
    }
}

class Bow extends Item {
    int storage = 5;

    public String display(){ return "Bow"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 0;
    }
}

class Arrow extends Item {
    int storage = 5;

    public String display(){ return "Arrow"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 0;
    }
}

class BowArrow extends Item {
    int storage = 5;

    public String display(){ return "Bow and Arrow"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 10;
    }
}

class EliteSword extends Item {
    int storage = 5;

    public String display(){ return "Elite Sword"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 15;
    }
}

class MagicStaff extends Item {
    int storage = 5;

    public String display(){ return "Magic Staff"; }
    public int storage(){ return storage; }
    int getDamage() {
        return 25;
    }
}


interface Hint {
    public void showHint();
    //Hints for each room
    // Spawn room: Explore the area, there are other rooms you can go to.
    // Map room: The tablet might come in handy later, keep it with you
    // Utility room:
    // Mysterious lake room: This water is not just for drinking, use it wisely and it will help you
    // Small animal room: You can either kill this animal or capture it for later
    // Mineral deposit room:
    // Volcano room: Keep the gem with you, it serves a greater purpose if used with something else
    // Armor room: You can craft armor, but make sure you use the correct material
    // Clearing room: The stones and wood found here can be used to craft things, as long as you have the proper quantities
    // Meteor room: Keep the ore with you, it serves a greater purpose if used with something else
    // Bone room: Search the room, there might be something useful hidden here
    // Mammoth room: With the right weapons the mammoth can be killed, but what purpose would that serve?
    // Dinosaur room: Remember that the T.Rex is hungry
    // Snake room: If you give the snake what it wants, it might do something for you in return
    // Checkpoint corridor: If you were to die in the following room you will respawn here with your belongings, but
    // beware you only have few lives!
    // Boss room: It would be easier to defeat the Reptile King if you had some allies
    // Mineral Deposit Room: Resources found here has the potential to be transformed into something essential later on
    // Utility Room: These items can prove to be of great use later on.
}

//Player class
class Player {
    int health = 100;
    int energy = 30;
    int water = 30;
    int capacity = 60;
    Room curr = new SpawnRoom();
    ArrayList<Item> inventory = new ArrayList<>();
    Stack<Room> path = new Stack<>();
    ArrayList<Room> path2 = new ArrayList<>();
    ArrayList<Animal> petsList = new ArrayList<>();
    Torch tor = new Torch();
    int i = 0;

    public Player() {
        path.push(curr);
        path2.add(curr);
        inventory.add(new Torch());
    }

    public String pickUpItem(Item it){
        inventory.add(it);
        return it + " has been picked up";
    }
    public void dropItem(String it){
        for(int i = 0; i < inventory.size(); i++){
            if(((inventory.get(i).display()).toLowerCase()).equals(it.toLowerCase())){
                inventory.remove(i);
                System.out.println(it + " has been dropped");
                return;
            }
        }

        System.out.println(it + " was never found");
    }

    public void eat(String it){

        for(int i = 0; i < inventory.size(); i++){
            if(((inventory.get(i).display()).toLowerCase()).equals(it.toLowerCase())){

                if(it.equals("fruit")){
                    energy += 5;
                }
                if(it.equals("exotic berry")){
                    energy += 7;
                }
                if(it.equals("meat")){
                    energy += 10;
                }

                inventory.remove(i);
                System.out.println(it + " has been eaten. Your current energy status is: " + energy + ". Current health status is: " + health);
                return;
            }
        }
        System.out.println(it + " was never found");
    }
    public void drink(String it){

        for(int i = 0; i < inventory.size(); i++){
            if(((inventory.get(i).display()).toLowerCase()).equals(it.toLowerCase())){

                if(it.equals("healing potion")){
                    health = 100;
                }

                System.out.println(inventory.get(i).display() + " has been consumed. Current health status is: " + health);
                inventory.remove(i);
                return;
            }
        }
        System.out.println(it + " was never found");
    }


    public int getHealth(){
        return health;
    }

    public void incHelath(){
        health += 10;
    }

    public void reduceEnergy(int e){

        if(e > energy){
            energy = 0;
            health -= Math.abs(energy-e);
            System.out.println("Your energy level is low. Please consume food now!");
        } else{
            energy -= e;
        }
    }

    public int getEnergy() { return energy; }

    public int getCapacity() { return capacity; }

    public void incCapacity(int newCap) {
        capacity += newCap;
    }

    public void decCapacity(int newCap) { capacity -= newCap; }

    public void takeDamage(int damage){
        health -= damage;
    }

    public Item changeTool(){
        i++;
        i %= (inventory.size());
        System.out.println("You are now using " + inventory.get(i).display());
        return inventory.get(i);
    }

    public Item getCurrentTool(){
        //i %= (inventory.size())+1;
        if(!inventory.isEmpty()){
            return inventory.get(i);
        }
        return null;
    }

    public void showInventory(){
        System.out.print("You currently have: ");
        if(inventory.isEmpty()){
            System.out.println();
        }
        for(int i = 0; i < inventory.size(); i++){
            if(i==inventory.size()-1){
                System.out.print(inventory.get(i).display() + "\n");
            } else{
                System.out.print(inventory.get(i).display() + ", ");
            }
        }
    }

    public void showPets(){
        System.out.print("Your pets: ");
        for(int i = 0; i < petsList.size(); i++){
            if(i==petsList.size()-1){
                System.out.print(petsList.get(i).getName() + "\n");
            } else{
                System.out.print(petsList.get(i).getName() + ", ");
            }
        }
    }

    public void pushRoom(Room r){
        path.push(r);
        path2.add(r);
    }

    public Room getCurrentRoom(){
        return curr;
    }

    public ArrayList<Room> getPath(){
        return path2;
    }

    public void setCurrentRoom(Room r){
        curr = r;
    }

    public int searchStack(Item i){
        return path.search(i);
    }

    public Room searchPath( String name){
        for(Room r : path2){
            if(r.roomName().equals(name)){
                return r;
            }
        }
        return null;
    }

    public void printStack(){
        for( Room m : path ){
            System.out.println(m.roomName());
        }
    }

    public Item searchItem(String name){
        for(Item i : inventory){
            if((i.display().toLowerCase()).equals(name.toLowerCase())){
                return i;
            }
        }
        return null;
    }

    public Animal searchAnimal(String name){
        for(Animal a : petsList){
            if((a.getName().toLowerCase()).equals(name.toLowerCase())){
                return a;
            }
        }
        return null;
    }

    public ArrayList<Item> getInventory(){
        return inventory;
    }


    public void tameAnimal(Animal tamedAnimal){
        petsList.add(tamedAnimal);
    }

    public ArrayList<Animal> getPetsList(){
        return petsList;
    }

    public Animal getPet(String name){
        for(Animal a : petsList){
            if(a.getName().toLowerCase().equals(name.toLowerCase())){
                return a;
            }
        }
        return null;
    }
}




////////////////////////////////////////////////////////////
//GAME OUTPUT
/*

"C:\Program Files\Java\jdk-13.0.1\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2021.2.1\lib\idea_rt.jar=50527:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2021.2.1\bin" -Dfile.encoding=UTF-8 -classpath "C:\Users\joela\IdeaProjects\Cave Escape\out\production\Cave Escape" com.company.Main
Welcome to Cave Escape! This is a text-based adventure game, so you need to type in commands to make perform an action.
Remember that with every action you take you will lose some energy, so be sure to eat food along the way.
If energy is too low or is depleted, you will start to lose health.
If you wish to view a list of possible commands at any time, enter in the command <instruction>
And now, the game begins....


You wake up in a dimly lit room, with nothing but a torch. There are four ways out of this room, the north entrance, south entrance, east entrance and west entrance.
So, enter in the desired direction you would like to go.

Enter your direction or command: instruction
The commands are:
attack: this command allows you to attack an animal
collect <enter corresponding number>: this command allows you to collect an item in a room
deploy <enter animal name>: this command allows you to use a pet to attack another animal
drink <name of drink>: this command allows you to consume items that are in a liquid state
drop <enter name of item>: this command allows you to drop an item from your inventory
eat <enter food name>: this command allows you to eat a food item
east: this command allows you to travel through the east path of a room
exit: this command exits the game
explore: this command shows you all the items a room contains
forge <enter tool name>: this command allows you to forge tools and weapons
hint: this command shows a hint for the room you are currently in
inventory: this command shows you all the items you currently have
north: this command allows you to travel through the north path of a room
show: this command shows you which rooms you have already visited
south: this command allows you to travel through the south path of a room
stats: this command shows you your current health, energy and carrying capacity
tame: this command allows you to tame an animal
toggle: this command allows you to switch between tools
unlock: this command allows you to unlock a lock if you have the correct key
west: this command allows you to travel through the west path of a room

Enter your direction or command: stats
Health: 100
Energy: 30
Capacity: 60

Enter your direction or command: south
You are now in Map Room.

Enter your direction or command: hint
Hint:
This room has objects useful for navigating the cave system.

Enter your direction or command: explore
Resources in this room:
[0] Map

Enter your direction or command: collect 0
You have now acquired map. Type in the command <map> in order to view its contents.
You currently have: Torch, Map

Enter your direction or command: map
To get back to Spawn Room, go north.
From Spawn Room, go east to get to Utility Room and then north from there to get to Clearing Room.
From Spawn Room, go north to get to Small Animal Room and then west from there to get to the Mineral Deposit Room.
Remember that you can go back to other rooms to get the necessary resources.

Enter your direction or command: north
You are now in Spawn Room.

Enter your direction or command: east
You are now in Utility Room.

Enter your direction or command: explore
Resources in this room:
[0] Bola
[1] Fruit
[2] Spear

Enter your direction or command: collect 2
You currently have: Torch, Map, Spear

Enter your direction or command: collect 1
You currently have: Torch, Map, Spear, Fruit

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Fruit, Bola

Enter your direction or command: north
You are now in Clearing Room.

Enter your direction or command: explore
Resources in this room:
[0] Fruit
[1] Wood
[2] Wood
[3] Wood
[4] Wood
[5] Wood
[6] Stone
[7] Stone
[8] Stone
[9] Stone
[10] Stone

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Fruit, Bola, Fruit

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Fruit, Bola, Fruit, Wood

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Fruit, Bola, Fruit, Wood, Wood

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Fruit, Bola, Fruit, Wood, Wood, Wood

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Fruit, Bola, Fruit, Wood, Wood, Wood, Wood

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Fruit, Bola, Fruit, Wood, Wood, Wood, Wood, Wood

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Fruit, Bola, Fruit, Wood, Wood, Wood, Wood, Wood, Stone

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Fruit, Bola, Fruit, Wood, Wood, Wood, Wood, Wood, Stone, Stone

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Fruit, Bola, Fruit, Wood, Wood, Wood, Wood, Wood, Stone, Stone, Stone

Enter your direction or command: inventory
You currently have: Torch, Map, Spear, Fruit, Bola, Fruit, Wood, Wood, Wood, Wood, Wood, Stone, Stone, Stone

Enter your direction or command: explore
Resources in this room:
[0] Stone
[1] Stone

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Fruit, Bola, Fruit, Wood, Wood, Wood, Wood, Wood, Stone, Stone, Stone, Stone

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Fruit, Bola, Fruit, Wood, Wood, Wood, Wood, Wood, Stone, Stone, Stone, Stone, Stone

Enter your direction or command: south
You are now in Utility Room.

Enter your direction or command: west
You are now in Spawn Room.

Enter your direction or command: west
You are now in Mysterious Lake Room.

Enter your direction or command: forge Axe
You have now acquired the Axe!

Enter your direction or command: forge Bow and Arrow
You have now acquired the Bow and Arrow!

Enter your direction or command: forge Hammer
You do not have the supplies to craft said tool

Enter your direction or command: inventory
You currently have: Torch, Map, Spear, Fruit, Bola, Fruit, Stone, Stone, Axe, Bow and Arrow

Enter your direction or command: drop stone
stone has been dropped

Enter your direction or command: drop stone
stone has been dropped

Enter your direction or command: stats
Health: 100
Energy: 8
Capacity: 27

Enter your direction or command: eat fruit
fruit has been eaten. Your current energy status is: 13. Current health status is: 100

Enter your direction or command: eat fruit
fruit has been eaten. Your current energy status is: 18. Current health status is: 100

Enter your direction or command: east
You are now in Spawn Room.

Enter your direction or command: north
You are now in Small Animal Room.

Enter your direction or command: explore
Resources in this room:
Nothing
Animals in this room:
Small Animal

Enter your direction or command: toggle
You are now using Map

Enter your direction or command: toggle
You are now using Spear

Enter your direction or command: toggle
You are now using Bola

Enter your direction or command: toggle
You are now using Axe

Enter your direction or command: toggle
You are now using Bow and Arrow

Enter your direction or command: attack
Your attack was successful! You have killed the Small Animal!
You have a remaining hp of 100

Enter your direction or command: explore
Resources in this room:
[0] Meat
You have defeated all the animals in this room!

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Bola, Axe, Bow and Arrow, Meat

Enter your direction or command: west
You are now in Mineral Deposit Room.

Enter your direction or command: explore
Resources in this room:
[0] Mineral ore
[1] Exotic Berry

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Bola, Axe, Bow and Arrow, Meat, Mineral ore

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Bola, Axe, Bow and Arrow, Meat, Mineral ore, Exotic Berry

Enter your direction or command: north
You are now in Volcano Room.

Enter your direction or command: hint
Hint:
This room is filled with lava; be careful where you step. Keep the gem with you, it will prove useful later on

Enter your direction or command: explore
Your energy level is low. Please consume food now!
Resources in this room:
[0] Gem

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Bola, Axe, Bow and Arrow, Meat, Mineral ore, Exotic Berry, Gem

Enter your direction or command: east
You are now in the Dinosaur Room.
Your energy level is low. Please consume food now!

Enter your direction or command: eat Exotic Berry
Exotic Berry has been eaten. Your current energy status is: 0. Current health status is: 96

Enter your direction or command: explore
Your energy level is low. Please consume food now!
Resources in this room:
Nothing
Animals in this room:
T-Rex

Enter your direction or command: tame
You have successfully tamed T-Rex

Enter your direction or command: pet
Your pets: T-Rex

Enter your direction or command: south
You are now in Bone Room.
Your energy level is low. Please consume food now!

Enter your direction or command: explore
Your energy level is low. Please consume food now!
Resources in this room:
[0] Checkpoint Corridor Key

Enter your direction or command: collect 0
You currently have: Torch, Map, Spear, Bola, Axe, Bow and Arrow, Mineral ore, Gem, Checkpoint Corridor Key

Enter your direction or command: west
You are now in Meteor Room.
Your energy level is low. Please consume food now!

Enter your direction or command: explore
Your energy level is low. Please consume food now!
Resources in this room:
[0] Reflective metal
[1] Exotic Berry

Enter your direction or command: collect 1
You currently have: Torch, Map, Spear, Bola, Axe, Bow and Arrow, Mineral ore, Gem, Checkpoint Corridor Key, Exotic Berry

Enter your direction or command: collect 0
Your capacity limit has been reached. Drop unwanted items or consume food items to free up space.

Enter your direction or command: drop torch
torch has been dropped

Enter your direction or command: drop map
map has been dropped

Enter your direction or command: drop Bola
Bola has been dropped

Enter your direction or command: eat Exotic Berry
Exotic Berry has been eaten. Your current energy status is: 0. Current health status is: 86

Enter your direction or command: collect 0
You currently have: Spear, Axe, Bow and Arrow, Mineral ore, Gem, Checkpoint Corridor, Reflective metal

Enter your direction or command: east
You are now in Bone Room.
Your energy level is low. Please consume food now!

Enter your direction or command: east
You are now in Mammoth Room.
Your energy level is low. Please consume food now!

Enter your direction or command: toggle
You are now using Reflective metal

Enter your direction or command: deploy T-Rex
Mammoth has attacked your pet back! Your pet has a remaining hp of 65
Mammoth is not dead yet. It has a remaining hp of 15

Enter your direction or command: deploy T-Rex
Your attack was successful! You have killed the Mammoth!
Your pet has a remaining hp of 65

Enter your direction or command: explore
Your energy level is low. Please consume food now!
Resources in this room:
[0] Meat
[1] Mammoth Tusks
You have defeated all the animals in this room!

Enter your direction or command: collet 0
Invalid command. Try Again.

Enter your direction or command: collect 0
You currently have: Spear, Axe, Bow and Arrow, Mineral ore, Gem, Checkpoint Corridor, Reflective metal, Meat

Enter your direction or command: collect 0
You currently have: Spear, Axe, Bow and Arrow, Mineral ore, Gem, Checkpoint Corridor, Reflective metal, Meat, Mammoth Tusks

Enter your direction or command: north
You are now in Snake Room.
Your energy level is low. Please consume food now!

Enter your direction or command: tame
You have successfully tamed Snake

Enter your direction or command: pet
Your pets: T-Rex, Snake

Enter your direction or command: explore
Your energy level is low. Please consume food now!
Resources in this room:
[0] Venom
Animals in this room:
Snake

Enter your direction or command: collect 0
Your capacity limit has been reached. Drop unwanted items or consume food items to free up space.

Enter your direction or command: inventory
You currently have: Spear, Axe, Bow and Arrow, Mineral ore, Gem, Checkpoint Corridor, Reflective metal, Mammoth Tusks

Enter your direction or command: drop Axe
Axe has been dropped

Enter your direction or command: collect 0
You currently have: Spear, Bow and Arrow, Mineral ore, Gem, Checkpoint Corridor, Reflective metal, Mammoth Tusks, Venom

Enter your direction or command: south
You are now in Mammoth Room.
Your energy level is low. Please consume food now!

Enter your direction or command: east
Your energy level is low. Please consume food now!
Entrance is blocked. Try a different direction.

Enter your direction or command: south
Your energy level is low. Please consume food now!
Entrance is blocked. Try a different direction.

Enter your direction or command: south
Your energy level is low. Please consume food now!
Entrance is blocked. Try a different direction.

Enter your direction or command: west
You are now in Bone Room.
Your energy level is low. Please consume food now!

Enter your direction or command: south
You are now in Clearing Room.
Your energy level is low. Please consume food now!

Enter your direction or command: south
You are now in Utility Room.
Your energy level is low. Please consume food now!

Enter your direction or command: west
You are now in Spawn Room.
Your energy level is low. Please consume food now!

Enter your direction or command: west
You are now in Mysterious Lake Room.
Your energy level is low. Please consume food now!

Enter your direction or command: explore
Your energy level is low. Please consume food now!
Resources in this room:
[0] Healing Potion

Enter your direction or command: collect 0
Your capacity limit has been reached. Drop unwanted items or consume food items to free up space.

Enter your direction or command: inventory
You currently have: Spear, Bow and Arrow, Mineral ore, Gem, Checkpoint Corridor, Reflective metal, Mammoth Tusks, Venom

Enter your direction or command: drop Bow and Arrow
Bow and Arrow has been dropped

Enter your direction or command: collect 0
You currently have: Spear, Bow and Arrow, Mineral ore, Gem, Checkpoint Corridor, Reflective metal, Mammoth Tusks, Venom, Healing Potion

Enter your direction or command: drink Healing Potion
Healing Potion has been consumed. Current health status is: 100

Enter your direction or command: east
You are now in Spawn Room.

Enter your direction or command: north
You are now in Small Animal Room.

Enter your direction or command: west
You are now in Mineral Deposit Room.

Enter your direction or command: north
You are now in Volcano Room.

Enter your direction or command: north
You are now in the Armor Room.

Enter your direction or command: hint
Hint:
This room is very beneficial to prepare for the final room of the game. Make sure to utilize the forge command. You can forge armor, an elite sword, or a magic staff, but make sure you use the correct material
Required material:
Elite Sword: Reflective Metal
Armor: Mineral Ore
Magic Staff: Mammoth Tusk, Gem

Enter your direction or command: forge Elite Sword
You have now acquired the Elite Sword!

Enter your direction or command: forge Armor
You have now acquired the Armor!

Enter your direction or command: forge Magic Staff
You have now acquired the Magic Staff!

Enter your direction or command: east
You have now reached the Checkpoint Corridor.

Enter your direction or command: north
You have to unlock the door first.
Entrance is blocked. Try a different direction.

Enter your direction or command: unlock
You have unlocked the lock!

Enter your direction or command: north
You have now reached the Boss.

Enter your direction or command: explore
Resources in this room:
Nothing
Animals in this room:
Reptile King

Enter your direction or command: deploy Snake
The Reptile King inflicted an attack of 18. Reptile King has an hp of 100 remaining.
Reptile King has attacked your pet back! Your pet has a remaining hp of 12
Reptile King is not dead yet. It has a remaining hp of 85

Enter your direction or command: toggle
You are now using Bow and Arrow

Enter your direction or command: toggle
You are now using Checkpoint Corridor Key

Enter your direction or command: toggle
You are now using Venom

Enter your direction or command: toggle
You are now using Elite Sword

Enter your direction or command: toggle
You are now using Armor

Enter your direction or command: toggle
You are now using Magic Staff

Enter your direction or command: attack
The Reptile King inflicted an attack of 23. Reptile King has an hp of 85 remaining.
Reptile King has attacked you back! You have a remaining hp of 97
Reptile King is not dead yet. It has a remaining hp of 60

Enter your direction or command: attack
The Reptile King inflicted an attack of 9. Reptile King has an hp of 60 remaining.
Reptile King has attacked you back! You have a remaining hp of 88
Reptile King is not dead yet. It has a remaining hp of 35

Enter your direction or command: attack
Your energy level is low. Please consume food now!
The Reptile King inflicted an attack of 2. Reptile King has an hp of 35 remaining.
Reptile King has attacked you back! You have a remaining hp of 83
Reptile King is not dead yet. It has a remaining hp of 10

Enter your direction or command: attack
Your energy level is low. Please consume food now!
Your attack was successful! You have killed the Reptile King!
You have a remaining hp of 80
You have won to game! Congratulations! Thank you for playing Cave Escape.

Process finished with exit code 0



 */