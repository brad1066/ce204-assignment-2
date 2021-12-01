class Edge implements Comparable<Edge> {
    int x, y; /* Endpoints */
    double w; /* Weight    */

    private Edge (int x, int y, double w) {
        this.x = x;  this.y = y;  this.w = w;
    }

    /*
     *  compareTo should return a negative intever if
     *  "this"<"that", 0 if they are equal, and a positive
     *  integer if "this">"that"
     */
    @Override
    public int compareTo (Edge that) {
	return 0;
    }
}
