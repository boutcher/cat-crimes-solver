public class CatCrimeSolver {

    static final String CAT_GINGER = "Ginger";
    static final String CAT_PIP_SQUEAK = "Pip Squeak";
    static final String CAT_MR_MITTENS = "Mr. Mittens";
    static final String CAT_DUCHESS = "Duchess";
    static final String CAT_SASSY = "Sassy";
    static final String CAT_TOM_CAT = "Tom Cat";

    // attributes
    private String BOW = "b";
    private String BLUEEYES = "B";
    private String LONGHAIR = "l";
    private String BELL = "k";
    private String STRIPES = "S";
    private String WHITEPAWS = "w";

    private Cat[] cat = new Cat[6];
    public int validSolutions = 0;
    private Cat[] tableCat = new Cat[6];
    int pnum = 0;

    public CatCrimeSolver() {
        cat[0] = new Cat(CAT_GINGER, BLUEEYES + STRIPES, 0);
        cat[1] = new Cat(CAT_PIP_SQUEAK, STRIPES + BELL, 1);
        cat[2] = new Cat(CAT_MR_MITTENS, WHITEPAWS + BELL, 2);
        cat[3] = new Cat(CAT_DUCHESS, LONGHAIR + BOW, 3);
        cat[4] = new Cat(CAT_SASSY, WHITEPAWS + LONGHAIR, 4);
        cat[5] = new Cat(CAT_TOM_CAT, BOW + BLUEEYES, 5);
    }

    // This method checks every permutation to see if it solves Cat Crime #40
    void checkSolution(int a[], int n) {
        pnum++;
        boolean conditionsMet;

        for (int i = 0; i < n; i++) {
            tableCat[i] = cat[a[i]];
        }

        // rule 1 - ginger not next to a bow wearer
        Cat ginger = find(CAT_GINGER);
        if (ginger.toTheLeft().has(BOW) || ginger.toTheRight().has(BOW)) return;

        // rule 2 - bell ball int front, at table (table positions 0, 4)
        if (!tableCat[0].has(BLUEEYES) && !tableCat[4].has(BLUEEYES)) return;

        // rule 3 - long haired cat is to the left of one wearing a bow
        conditionsMet = false;
        for (int x = 0; x < 6; x++) {
            if (tableCat[x].has(BOW) && tableCat[x].toTheLeft().has(LONGHAIR)) conditionsMet = true;
        }
        if (!conditionsMet) return;

        // rule 4 - No long hairs across from blue eyes
        for (int x = 0; x < 6; x++) {
            Cat c = tableCat[x];
            if (c.has(BLUEEYES) && c.acrossFrom().has(LONGHAIR)) return;
        }

        // rule 5 - cat w/ bow across from cat wearing bell and to the right of stripes
        conditionsMet = false;
        for (int x = 0; x < 6; x++) {
            Cat c = tableCat[x];
            if (c.has(BOW) && c.acrossFrom().has(BELL) && c.toTheLeft().has(STRIPES)) conditionsMet = true;
        }
        if (!conditionsMet) return;

        // rule 6 - cat w/ bow 3 seats from a cat with stripes, and to the left of cat w/ white paws
        // fun fact - this rule isn't needed at all... the other rules get you to one solution by themselves
        conditionsMet = false;
        for (int x = 0; x < 6; x++) {
            Cat c = tableCat[x];
            if (tableCat[(x + 3) % 6].has(STRIPES) && c.toTheRight().has(WHITEPAWS)) conditionsMet = true;
        }
        if (!conditionsMet) return;

        // rule 7
        conditionsMet = false;
        for (int x = 0; x < 6; x++) {
            Cat c = tableCat[x];
            Cat ll = c.toTheLeft().toTheLeft();
            Cat rr = c.toTheRight().toTheRight();

            if (c.has(STRIPES)) {
                if ((ll.has(WHITEPAWS) && rr.has(BELL)) || rr.has(WHITEPAWS) && ll.has(BELL)) conditionsMet = true;
            }
        }
        if (!conditionsMet) return;

        System.out.print("#" + pnum + " Possible: ");
        for (int x = 0; x < 6; x++) {
            System.out.print(tableCat[x] + " ");
        }
        System.out.println();

        validSolutions++;
    }

    Cat find(String name) {
        for (int a = 0; a < 6; a++) {
            if (tableCat[a].name.equals(name)) return tableCat[a];
        }
        return null;
    }

    int leftOf(int a) {
        return (a + 7) % 6;
    }

    int rightOf(int a) {
        return (a + 5) % 6;
    }

    int across(int a) {
        return (new int[]{3, 5, 4, 0, 2, 1})[a];
    }

    // Generating permutation using Heap Algorithm, stolen from the interwebs
    void heapPermutation(int a[], int size, int n) {
        // if size becomes 1 then prints the obtained
        // permutation
        if (size == 1)
            checkSolution(a, n);

        for (int i = 0; i < size; i++) {
            heapPermutation(a, size - 1, n);

            // if size is odd, swap 0th i.e (first) and
            // (size-1)th i.e (last) element
            if (size % 2 == 1) {
                int temp = a[0];
                a[0] = a[size - 1];
                a[size - 1] = temp;
            }

            // If size is even, swap ith
            // and (size-1)th i.e last element
            else {
                int temp = a[i];
                a[i] = a[size - 1];
                a[size - 1] = temp;
            }
        }
    }

    class Cat {
        String name;
        String attrs;
        int position;

        public Cat(String n, String a, int p) {
            name = n;
            attrs = a;
            position = p;
        }

        public boolean has(String attr) {
            return attrs.contains(attr);
        }

        public int whereamI() {
            for (int a = 0; a < 6; a++) {
                if (tableCat[a].name.equals(name)) return a;
            }
            return -1;
        }

        public String toString() {
            return "[" + name + "]";
        }

        public Cat toTheLeft() {
            return tableCat[leftOf(whereamI())];
        }

        public Cat toTheRight() {
            return tableCat[rightOf(whereamI())];
        }

        public Cat acrossFrom() {
            return tableCat[across(whereamI())];
        }
    }

    public static void main(String args[]) {
        CatCrimeSolver self = new CatCrimeSolver();

        int a[] = {0, 1, 2, 3, 4, 5};
        self.heapPermutation(a, a.length, a.length);

        System.out.println("Valid solutions: " + self.validSolutions + " / " + self.pnum);
    }

}
