package openjade.task.behaviour;

public enum AbilityConfig {
	
	//          speed   capacity 	completed  	range 		points 	range
	TERRIBLE	(400,	3000, 	 	15, 		5,  		15,		5 ),
	BAD			(300,	3000, 	 	35, 		5,  		35,		5 ),
	MODERATE	(200,	3000, 	 	55, 		5,  		55,		5 ),
	GOD			(100,	3000, 	 	75, 		5,  		75,		5 ),
	EXCELLENT	( 50, 	3000, 	 	95, 		5,  		95,	5 );

	private int speed;
	private int capacity;
	private int completed;
	private int completedRange;
	private int points;
	private int pointsRange;
	
	AbilityConfig(int _speed, int _capacity, int _completed, int _completedRange, int _points, int _pointsRange){
		this.speed = _speed;
		this.capacity = _capacity;
		this.completed = _completed;
		this.completedRange = _completedRange;
		this.points = _points;
		this.pointsRange = _pointsRange;
	}
	
	public long speed() {
		return speed;
	}

	public long capacity() {
		return capacity;
	}

	
	public int completed() {
		return completed;
	}

	public int completedRange() {
		return completedRange;
	}

	public float points() {
		return points;
	}

	public float pointsRange() {
		return pointsRange;
	}
}

