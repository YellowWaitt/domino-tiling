package application.model.algorithms;

public enum Direction {

	UP {
		@Override
		public Direction left() {
			return LEFT;
		}
		@Override
		public Direction opposite() {
			return DOWN;
		}
	},

	DOWN {
		@Override
		public Direction left() {
			return RIGHT;
		}
		@Override
		public Direction opposite() {
			return UP;
		}
	},

	LEFT {
		@Override
		public Direction left() {
			return DOWN;
		}
		@Override
		public Direction opposite() {
			return RIGHT;
		}
	},

	RIGHT {
		@Override
		public Direction left() {
			return UP;
		}
		@Override
		public Direction opposite() {
			return LEFT;
		}
	},

	UNKNOW {
		@Override
		public Direction left() {
			return UNKNOW;
		}
		@Override
		public Direction opposite() {
			return UNKNOW;
		}
	};

	public static Direction getDirectionFromChar(char c) {
		switch (c) {
		case 'H':
		case 'h':
			return Direction.UP;
		case 'B':
		case 'b':
			return Direction.DOWN;
		case 'D':
		case 'd':
			return Direction.RIGHT;
		case 'G':
		case 'g':
			return Direction.LEFT;
		default:
			return Direction.UNKNOW;
		}
	}

	public abstract Direction left();

	public abstract Direction opposite();
}
