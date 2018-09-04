class ForLooper {
    public static void main(String[] args) {
        System.out.print(" *");
        for(int i = 0; i <= 12; i++) // Top Header
            System.out.printf("%5d", i);
        
        System.out.println();

        for(int i = 0; i <= 12; i++) { // Left number
            System.out.printf("%2d", i);
            for(int j = 0; j <= 12; j++)  // Top number
                System.out.printf("%5d", j * i); // Could work with 4 spaces of padding, but 5 looks better

            System.out.println();
        }
            
    }
}