import java.util.Arrays;
import java.util.Random;
import java.io.*;


public
    class S22083 {

    public static void main(String[] args) {

        Container container = returnRandomContainer();
        Goods[] goods = returnGoodTable();
        loadContainerWithProperGoods(container, goods);

        containersToFile();
        readContainersFromFileAndPrintMessage();

        OOCLHongKong ship = new OOCLHongKong();
        ship.loadedContainers = new Container[15000];
        loading(ship);

        makeManifest(ship);
    }

    public static int twentyOrForty() {
        Random random = new Random();
        int rand = random.nextInt(5);
        if (rand < 3) {
            return 20;
        }
        return 40;
    }

    public static Container returnRandomContainer() {
        Random random = new Random();
        int numberOfAvailableContainers = 10;
        Container[] containers = new Container[numberOfAvailableContainers];
        containers[0] = new GeneralPurposeContainer(twentyOrForty());
        containers[1] = new OpenTopContainer(twentyOrForty());
        containers[2] = new OpenSidedContainer(twentyOrForty());
        containers[3] = new FlatRackContainer(twentyOrForty());
        containers[4] = new PlatformContainer(twentyOrForty());
        containers[5] = new TankContainer();
        containers[6] = new RefrigeratedContainer(twentyOrForty());
        containers[7] = new VentilatedContainer();
        containers[8] = new BulkContainer();
        containers[9] = new GarmentsOnHangersContainer(twentyOrForty());
        int randomIndex = random.nextInt(numberOfAvailableContainers);
        return containers[randomIndex];
    }

    public static Goods[] returnGoodTable() {
        int numberOfGoods = 14;
        Goods[] goods = new Goods[numberOfGoods];
        goods[0] = new Boxes();
        goods[1] = new Buses();
        goods[2] = new Cheese();
        goods[3] = new ChickenMeat();
        goods[4] = new ClotherOnHanger();
        goods[5] = new Coal();
        goods[6] = new CoffeeBeans();
        goods[7] = new Juice();
        goods[8] = new Machinery();
        goods[9] = new Milk();
        goods[10] = new OilEquipment();
        goods[11] = new Pipes();
        goods[12] = new Spices();
        goods[13] = new Tires();
        return goods;
    }

    public static Container loadContainer(Container container, Goods goods) {
        container.getTotalWeight();
        container.loadedWith = goods.getGoodsName();
        container.loadGoods = goods;
        return container;
    }

    public static Container loadContainerWithProperGoods(Container container, Goods[] goods) {
        int containerNumber = container.containerNumber;
        Goods[] goodsProperForThisContainer = new Goods[goods.length];
        int ind = 0;
        for (Goods good : goods) {
            if (good.getContainerIndex() == containerNumber) {
                goodsProperForThisContainer[ind] = good;
                ind++;
            }
        }
        Random random = new Random();
        int randomIndex;
        if (ind < 1) {
            randomIndex = 0;
        } else {
            randomIndex = random.nextInt(ind);
        }
        Goods selected = goodsProperForThisContainer[randomIndex];
        return loadContainer(container, selected);
    }

    public static void containersToFile() {
        try {
            FileWriter fw = new FileWriter("containers.txt");
            FileOutputStream fos = new FileOutputStream("objects.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (int i = 0; i < 15000; i++) {
                Container container = loadContainerWithProperGoods(returnRandomContainer(), returnGoodTable());
                fw.write("id" + (i + 1) + "     " + loadedContainerToString(container));
                oos.writeObject(container);
            }
            fw.close();
            oos.close();
            System.out.println("Pomyślnie zapisano listę kontenerów w pliku containers.txt");
        } catch (IOException e) {
            System.out.println("Błąd");
            e.printStackTrace();
        }
    }
    public static Container[] containersFromFile() {
        int numOfContainers = 15000;
        Container[] containers = new Container[numOfContainers];
        try {
            FileInputStream fis = new FileInputStream("objects.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            for (int i = 0; i < numOfContainers; i++) {
                containers[i] = (Container) ois.readObject();
            }
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return containers;
    }

    public static void readContainersFromFileAndPrintMessage(){
        containersFromFile();
        System.out.println("Pomyślnie wczytano listę kontenerów z pliku");
    }


    public static String loadedContainerToString(Container container) {
        return "container: " + container.loadGoods.getContainer() +
                " size " + container.size + "; " +
                "loaded with: " + container.loadGoods.getGoodsName() + "; " +
                "weight: " + container.getTotalWeight() + " kg" + "\n";
    }

    public static Container[] sortByWeightFromHighest(Container[] containers) {
        int indMax;
        for (int i = 0; i < containers.length - 1; i++) {
            indMax = i;
            for (int j = indMax + 1; j < containers.length; j++) {
                if (containers[indMax].getTotalWeight() < containers[j].getTotalWeight()) {
                    indMax = j;
                }
            }
            Container temp = containers[indMax];
            containers[indMax] = containers[i];
            containers[i] = temp;
        }
        return containers;
    }

    public static Container[] selectSize20Or40(Container[] containers, double size) {
        int count = 0;
        for (Container container : containers) {
            if (container.size == size) {
                count++;
            }
        }
        Container[] selectedSizeContainers = new Container[count];
        count = 0;
        for (Container container : containers) {
            if (container.size == size) {
                selectedSizeContainers[count++] = container;
            }
        }
        return selectedSizeContainers;
    }

    public static Container[] selectRefrigeratedContainers(Container[] containers) {
//        Container[] containers = containersFromFile();
        int count = 0;
        for (Container container : containers) {
            if (container.isRefrigerated) {
                count++;
            }
        }
        Container[] refrigeratedContainers = new Container[count];
        count = 0;
        for (Container container : containers) {
            if (container.isRefrigerated) {
                refrigeratedContainers[count++] = container;
            }
        }
        return refrigeratedContainers;
    }


    public static Container[] selectCloseTopExcludingRefrigerated(Container[] containers) {
        int count = 0;
        for (Container container : containers) {
            if ((container.isCloseTop) && (!container.isRefrigerated)) {
                count++;
            }
        }
        Container[] closeTopExcludingRefrigerated = new Container[count];
        count = 0;
        for (Container container : containers) {
            if ((container.isCloseTop) && (!container.isRefrigerated)) {
                closeTopExcludingRefrigerated[count++] = container;
            }
        }
        return closeTopExcludingRefrigerated;
    }

    public static Container[] selectOpenTop(Container[] containers) {
        int count = 0;
        for (Container c : containers) {
            if (c.isOpenTop) {
                count++;
            }
        }
        Container[] openTop = new Container[count];
        count = 0;
        for (Container container : containers) {
            if (container.isOpenTop) {
                openTop[count++] = container;
            }
        }
        return openTop;
    }

    public static Container[] selectTank(Container[] containers) {
        int count = 0;
        for (Container container : containers) {
            if (container.isTank) {
                count++;
            }
        }
        Container[] tankContainers = new Container[count];
        count = 0;
        for (Container container : containers) {
            if (container.isTank) {
                tankContainers[count++] = container;
            }
        }
        return tankContainers;
    }


    public static Container[] getRefrigerated40Sorted() {
        return sortByWeightFromHighest(selectSize20Or40(selectRefrigeratedContainers(containersFromFile()), 40));
    }

    public static Container[] getRefrigerated20Sorted() {
        return sortByWeightFromHighest(selectSize20Or40(selectRefrigeratedContainers(containersFromFile()), 20));
    }

    public static Container[] getCloseTop40Sorted() {
        return sortByWeightFromHighest(selectSize20Or40(selectCloseTopExcludingRefrigerated(containersFromFile()), 40));
    }

    public static Container[] getCloseTop20Sorted() {
        return sortByWeightFromHighest(selectSize20Or40(selectCloseTopExcludingRefrigerated(containersFromFile()), 20));
    }

    public static Container[] getOpenTop40Sorted() {
        return sortByWeightFromHighest(selectSize20Or40(selectOpenTop(containersFromFile()), 40));
    }

    public static Container[] getOpenTop20Sorted() {
        return sortByWeightFromHighest(selectSize20Or40(selectOpenTop(containersFromFile()), 20));
    }

    public static Container[] getTankSorted() {
        return sortByWeightFromHighest(selectSize20Or40(selectTank(containersFromFile()), 20));

    }

    public static String setContainerNumber(Container container, int numberInList){
        String fiveDigits;
        if(numberInList < 10){
            fiveDigits = "N0000";
        } else if (numberInList < 100){
            fiveDigits = "N000";
        } else if (numberInList < 1000){
            fiveDigits = "N00";
        } else if (numberInList < 10000){
            fiveDigits = "N0";
        } else{
            fiveDigits = "N";
        }
        return fiveDigits + numberInList + "SIZE" + (int)container.size;
    }

    public static void setPosition(Container container, int bay, int row, int tier){
        container.position = new int[3];
        container.position[0] = bay;
        container.position[1] = row;
        container.position[2] = tier;
    }

    public static void loading(OOCLHongKong ship) {

        Container[] refrigerated20Sorted = getRefrigerated20Sorted();
        Container[] refrigerated40Sorted = getRefrigerated40Sorted();
        Container[] closeTop20Sorted = getCloseTop20Sorted();
        Container[] closeTop40Sorted = getCloseTop40Sorted();
        Container[] openTop20Sorted = getOpenTop20Sorted();

        Container[] openTop40Sorted = getOpenTop40Sorted();
        Container[] tankSorted = getTankSorted();

        int bayMax = 24;
        int rowMax = 23;
        int tierMax = 30;

        int ref20Counter = 0;
        int close20Counter = 0;
        int open20Counter = 0;
        int tankCounter = 0;

        int ref40Counter = 0;
        int close40Counter = 0;
        int open40Counter = 0;

        int loadedContainersCounter = 0;

        ship.loadedContainers = new Container[15000];


        for (int i = 1; i < tierMax + 1; i++) {
            for (int j = 1; j < rowMax + 1; j++) {
                for (int k = 1; k < bayMax + 1; k++) {

                    if ((k % 2 == 0) && (open40Counter < openTop40Sorted.length)) {
                        if (ref40Counter < refrigerated40Sorted.length) {
                            setPosition(refrigerated40Sorted[ref40Counter], k, j, i);
                            ship.loadedContainers[loadedContainersCounter++] = refrigerated40Sorted[ref40Counter];
                            ref40Counter++;
                        } else if ((ref40Counter == refrigerated40Sorted.length) && (close40Counter < closeTop40Sorted.length)) {
                            setPosition(closeTop40Sorted[close40Counter], k, j, i);
                            ship.loadedContainers[loadedContainersCounter++] = closeTop40Sorted[close40Counter];
                            close40Counter++;
                        } else if ((close40Counter == closeTop40Sorted.length) && (open40Counter < openTop40Sorted.length)) {
                            setPosition(openTop40Sorted[open40Counter], k, j, i);
                            ship.loadedContainers[loadedContainersCounter++] = openTop40Sorted[open40Counter];
                            open40Counter++;

                        }

                    } else {
                        if (ref20Counter < refrigerated20Sorted.length) {
                            setPosition(refrigerated20Sorted[ref20Counter], k, j, i);
                            ship.loadedContainers[loadedContainersCounter++] = refrigerated20Sorted[ref20Counter];
                            ref20Counter++;
                        } else if ((ref20Counter == refrigerated20Sorted.length) && (close20Counter < closeTop20Sorted.length)) {
                            setPosition(closeTop20Sorted[close20Counter], k, j, i);
                            ship.loadedContainers[loadedContainersCounter++] = closeTop20Sorted[close20Counter];
                            close20Counter++;

                        } else if ((close20Counter >= closeTop20Sorted.length) && (open20Counter < openTop20Sorted.length)) {
                            setPosition(openTop20Sorted[open20Counter], k, j, i);
                            ship.loadedContainers[loadedContainersCounter++] = openTop20Sorted[open20Counter];
                            open20Counter++;
                        } else if ((open20Counter >= openTop20Sorted.length) && (tankCounter < tankSorted.length)) {
                            setPosition(tankSorted[tankCounter], k, j, i);
                            ship.loadedContainers[loadedContainersCounter++] = tankSorted[tankCounter];
                            tankCounter++;
                        }
                    }
                }
            }
        }
    }





    public static void makeManifest(Ship ship){
        try{
            FileWriter fw = new FileWriter("manifest.txt");
            fw.write("SHIP NAME: " + ship.shipName + "\n");
            fw.write("NUMER KONTENERA" + "     " + "POZYCJA:" + "       " + "WAGA" + "          " + "TOWAR" + "\n");
            fw.write("                  BAY ROW TIER " + "\n");

            for (int i = 0; i < 15000; i++) {
                fw.write( setContainerNumber(ship.loadedContainers[i], (i+1)) + "       " + Arrays.toString(ship.loadedContainers[i].position) +
                        "       " + ship.loadedContainers[i].getTotalWeight() + "       " + ship.loadedContainers[i].loadGoods.getGoodsName() + "\n");
            }
            fw.close();
            System.out.println("Plik manifest.txt został utworzony");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

class Container implements Serializable{

    protected double size;
    protected double volume;
    protected double topUpWeight;
    protected double lengthInternal;
    protected double widthInternal;
    protected double heightInternal;
    protected double totalMass;
    protected double containerMass;
    protected int containerNumber;
    protected String loadedWith;
    protected Goods loadGoods;
    protected boolean isRefrigerated;
    protected boolean isCloseTop;
    protected boolean isOpenTop;
    protected boolean isTank;
    protected int[] position = new int[3];

    public double getTotalWeight() {
        this.totalMass = (this.containerMass + this.topUpWeight);
        return this.totalMass;
    }
}

class BulkContainer extends Container {
    public BulkContainer(){
        super.size = 20;
        super.containerNumber = 9;
        super.volume = 35;
        super.topUpWeight = 32275;
        super.lengthInternal = 5900;
        super.widthInternal = 2348;
        super.heightInternal = 2503;
        super.containerMass = 2725;
        super.isCloseTop = true;
    }
}
class TankContainer extends Container {
    protected double wallThickness;
    protected double thermalIsolation;
    protected double pressureOp;

    public TankContainer(){
        super.size = 20;

        super.containerNumber = 6;
        super.isTank = true;
        this.wallThickness = 4;
        this.thermalIsolation = 0.7;
        this.pressureOp = 3;

        super.volume = 25;
        super.topUpWeight = 32350;
        super.lengthInternal = 2438;
        super.widthInternal = 6096;
        super.heightInternal = 2591;
        super.containerMass = 3000;
    }
}


class FlatRackContainer extends Container {

    public FlatRackContainer(double size){
        super.size = size;
        super.containerNumber = 4;
        super.isOpenTop = true;
        if(size == 20) {
            super.volume = 32.5;
            super.topUpWeight = 21100;
            super.lengthInternal = 5900;
            super.widthInternal = 2397;
            super.heightInternal = 2303;
            super.containerMass = 2900;
        }
        if(size == 40){
            super.volume = 65;
            super.topUpWeight = 26280;
            super.lengthInternal = 12035;
            super.widthInternal = 2335;
            super.heightInternal = 2315;
            super.containerMass = 5870;
        }
    }
}

class GarmentsOnHangersContainer extends Container {

    public GarmentsOnHangersContainer(double size){
        super.size = size;
        super.containerNumber = 10;
        super.isCloseTop = true;
        if (size == 20) {
            super.volume = 33.2;
            super.topUpWeight = 28180;
            super.lengthInternal = 5900;
            super.widthInternal = 2350;
            super.heightInternal = 2390;
            super.containerMass = 2300;

        }
        if(size == 40){
            super.volume = 67.7;
            super.topUpWeight = 28750;
            super.lengthInternal = 12030;
            super.widthInternal = 2350;
            super.heightInternal = 2390;
            super.containerMass = 3750;

        }
    }
}

class GeneralPurposeContainer extends Container {



    public GeneralPurposeContainer(double size){
        super.size = size;
        super.containerNumber = 1;
        super.isCloseTop = true;
        if(size == 20) {
            super.volume = 32.3;
            super.topUpWeight = 21687;
            super.lengthInternal = 5918;
            super.widthInternal = 2330;
            super.heightInternal = 2356;
            super.containerMass = 2370;
        }
        if(size == 40){
            super.volume = 66.9;
            super.topUpWeight = 26380;
            super.lengthInternal = 12015;
            super.widthInternal = 2330;
            super.heightInternal = 2389;
            super.containerMass = 4000;
        }
    }
}

class OpenSidedContainer extends Container {

    public OpenSidedContainer(double size){
        super.size = size;
        super.containerNumber = 3;
        super.isOpenTop = true;
        if(size == 20) {
            super.volume = 31;
            super.topUpWeight = 20830;
            super.lengthInternal = 5898;
            super.widthInternal = 2287;
            super.heightInternal = 2302;
            super.containerMass = 3170;
        }
        if(size == 40){
            super.volume = 64;
            super.topUpWeight = 26600;
            super.lengthInternal = 12032;
            super.widthInternal = 2350;
            super.heightInternal = 2695;
            super.containerMass = 3880;

        }
    }
}


class OpenTopContainer extends Container{


    public OpenTopContainer(double size){
        super.size = size;
        super.containerNumber = 2;
        super.isOpenTop = true;
        if(this.size == 20){
            super.volume = 32.3;
            super.topUpWeight = 18140;
            super.lengthInternal = 5885;
            super.widthInternal = 2319;
            super.heightInternal = 2366;
            super.containerMass = 2580;
        }
        if(this.size == 40){
            super.volume = 66.7;
            super.topUpWeight = 26350;
            super.lengthInternal = 12017;
            super.widthInternal = 2333;
            super.heightInternal = 2318;
            super.containerMass = 4290;
        }
    }
}


class PlatformContainer extends Container {
    boolean open;

    public PlatformContainer(double size){
        super.size = size;
        super.containerNumber = 5;
        super.isOpenTop = true;
        if(size == 20){
            super.topUpWeight = 31260;
            super.lengthInternal = 6060;
            super.widthInternal = 2440;
            this.open = true;
            super.containerMass = 2740;
        }
        if(size == 40){
            super.topUpWeight = 39300;
            super.lengthInternal = 12190;
            super.widthInternal = 2440;
            this.open = true;
            super.containerMass = 5700;
        }
    }
}

class RefrigeratedContainer extends Container {

    public RefrigeratedContainer(double size) {
        super.size = size;
        super.isRefrigerated = true;
        super.containerNumber = 7;
        if (size == 20) {
            super.volume = 26.6;
            super.topUpWeight = 20950;
            super.lengthInternal = 5677;
            super.widthInternal = 2236;
            super.heightInternal = 2077;
            super.containerMass = 3050;
        }
        if (size == 40) {
            super.volume = 34.2;
            super.topUpWeight = 25960;
            super.lengthInternal = 11690;
            super.widthInternal = 2250;
            super.heightInternal = 2247;
            super.containerMass = 4520;

        }
    }
}

class VentilatedContainer extends Container {
    protected boolean isVentilated;
    public VentilatedContainer(){
        super.size = 20;
        this.isVentilated = true;

        super.containerNumber = 8;
        super.isCloseTop = true;

        super.volume = 33;
        super.topUpWeight = 21727;
        super.lengthInternal = 5888;
        super.widthInternal = 2125;
        super.heightInternal = 2392;
        super.containerMass = 2394;
    }
}




class Goods implements Serializable{
    protected int containerIndex;
    protected String container;
    protected String goodsName;

    public int getContainerIndex() {
        return containerIndex;
    }

    public String getContainer() {
        return container;
    }

    public String getGoodsName() {
        return goodsName;
    }
}


class Boxes extends Goods {

    public Boxes() {
        super.containerIndex = 1;
        super.container = "General purpose container";
        super.goodsName = "Boxes";
    }
}

class Buses extends Goods {

        public Buses(){
            super.containerIndex = 4;
            super.container = "Flat Rack Container";
            super.goodsName = "Buses";
        }
}

class Cheese extends Goods {
        protected boolean needFrigde;
        public Cheese(){
            super.containerIndex = 7;
            super.container = "Refrigerated container";
            super.goodsName = "Cheese";
            this.needFrigde = true;
        }
}


class ChickenMeat extends Goods {
    protected boolean needFridge;
    public ChickenMeat(){
        super.containerIndex = 7;
        super.container = "Refrigerated container";
        super.goodsName = "Chicken meat";
        this.needFridge = true;
    }
}

class ClotherOnHanger extends Goods {
    protected boolean isGarmentsOnHanger;
    public ClotherOnHanger(){
        super.containerIndex = 10;
        super.container = "Garments on hanger container";
        super.goodsName = "Clothes on hanger";
        this.isGarmentsOnHanger = true;
    }
}

class Coal extends Goods {
    protected boolean isBulk;
    public Coal(){
        super.containerIndex = 9;
        super.container = "Bulk container";
        super.goodsName = "Coal";
        this.isBulk = true;
    }
}

class CoffeeBeans extends Goods {
    protected boolean needVentilation;
    public CoffeeBeans(){
        super.containerIndex = 8;
        super.container = "Ventilated container";
        super.goodsName = "Coffee beans";
        this.needVentilation = true;
    }
}

class Juice extends Goods {
    protected boolean Isliquid;
    public Juice(){
        super.containerIndex = 6;
        super.container = "Tank Container";
        super.goodsName = "Juice";
        this.Isliquid = true;
    }
}

class Machinery extends Goods {
    public Machinery(){
        super.containerIndex = 3;
        super.container = "Open sided container";
        super.goodsName = "Machinery";
    }
}

class Milk extends Goods {
    protected boolean Isliquid;
    public Milk(){
        super.containerIndex = 6;
        super.container = "Tank container";
        super.goodsName = "Milk";
        this.Isliquid = true;
    }
}

class OilEquipment extends Goods {
    public OilEquipment(){
        super.containerIndex = 5;
        super.container = "Platform Container";
        super.goodsName = "Oil equipment";
    }
}

class Pipes extends Goods {
    public Pipes(){
        super.containerIndex = 4;
        super.container = "Flat rack Container";
        super.goodsName = "Pipes";
    }
}

class Spices extends Goods {
    protected boolean isBulk;
    public Spices(){
        super.containerIndex = 9;
        super.container = "Bulk container";
        super.goodsName = "Spices";
        this.isBulk = true;
    }
}

class Tires extends Goods {
    public Tires(){
        super.containerIndex = 2;
        super.container = "Open Top Container";
        super.goodsName = "Tires";
    }
}


class Ship {
    protected String shipName;
    protected int numOfBays;
    protected int numOfRows;
    protected int numOfTiers;
    public Container[] loadedContainers = new Container[15000];
}

class OOCLHongKong extends Ship {

    public OOCLHongKong() {
        super.shipName = "OOCLHongKong";
        super.numOfBays = 24;
        super.numOfRows = 23;
        super.numOfTiers = 30;
    }
}


/*
porządek ładowania:
kontenery mają 3 współrzędne - bay, row, tier.

kontenery są układane rzędami (najpierw powiększa się BAY;
następnie, kiedy wszystkie BAY w rzędzie są zajęte, powiększa się
ROW; kiedy wszystkie ROW w TIER są zajęte, powiększa się współrzędna TIER)

 20' kontenery są układane na nieparzyste BAY
 40' kontenery są układane na parzyste BAY

 kontenery z chłodzeniem muszą byc blisko zasilaczy - zakładam, że są one na dole,
 czyli są układane najpierw.

 Najcięższe kontenery są układane w pierwszej kolejnośći

 */
