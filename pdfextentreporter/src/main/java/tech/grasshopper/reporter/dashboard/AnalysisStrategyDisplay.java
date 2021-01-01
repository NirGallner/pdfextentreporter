package tech.grasshopper.reporter.dashboard;

import com.aventstack.extentreports.AnalysisStrategy;

public enum AnalysisStrategyDisplay {

	BDD {
		public String firstLevelText() {
			return "Features";
		}

		public String secondLevelText() {
			return "Scenarios";
		}

		public String thirdLevelText() {
			return "Steps";
		}

		public String logsText() {
			return "";
		}

		public boolean displayLogsChart() {
			return false;
		}

		public int firstLevelChartXLocation() {
			return 50;
		}

		public int secondLevelChartXLocation() {
			return 300;
		}

		public int thirdLevelChartXLocation() {
			return 550;
		}

		public int logsChartXLocation() {
			return 0;
		}

		public int chartWidth() {
			return 235;
		}
	},
	TEST {

		public String firstLevelText() {
			return "Tests";
		}

		public String secondLevelText() {
			return "Steps";
		}
	},
	CLASS {

		public String firstLevelText() {
			return "Class";
		}

		public String secondLevelText() {
			return "Methods";
		}
	},
	SUITE {

		public String firstLevelText() {
			return "Suites";
		}

		public String secondLevelText() {
			return "Class";
		}

		public String thirdLevelText() {
			return "Tests";
		}
	};

	public abstract String firstLevelText();

	public abstract String secondLevelText();

	public String thirdLevelText() {
		return "";
	}

	public boolean displayLogsChart() {
		return true;
	}

	public String logsText() {
		return "Log Events";
	}

	public int firstLevelChartXLocation() {
		return 50;
	}

	public int secondLevelChartXLocation() {
		return 240;
	}

	public int thirdLevelChartXLocation() {
		return 430;
	}

	public int logsChartXLocation() {
		return 620;
	}

	public int chartWidth() {
		return 170;
	}

	public static AnalysisStrategyDisplay displaySettings(AnalysisStrategy strategy) {
		switch (strategy) {
		case BDD:
			return AnalysisStrategyDisplay.BDD;
		case SUITE:
			return AnalysisStrategyDisplay.SUITE;
		case CLASS:
			return AnalysisStrategyDisplay.CLASS;
		default:
			return AnalysisStrategyDisplay.TEST;
		}
	}
}
