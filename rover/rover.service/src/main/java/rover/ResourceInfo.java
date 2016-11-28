package rover;

public class ResourceInfo {

	private double x;
	private double y;
	
	private int count;

        private int type;
	
	public ResourceInfo(double x, double y, int count) {
		this(x, y, count, 1);
	}

	public ResourceInfo(double x, double y, int count, int type) {
		this.x = x;
		this.y = y;
		this.count = count;
                this.type = type;
	}

        public int getType() {
                return type;
        }

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
		
	
}
