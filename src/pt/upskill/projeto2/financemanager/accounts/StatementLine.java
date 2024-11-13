package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.date.Date;


public class StatementLine implements Comparable<StatementLine> {

	private Date date;
	private Date valueDate;
	private String description;
	private double draft;
	private double credit;
	private double accountingBalance;
	private double availableBalance;
	private Category category;

	public static StatementLine newStatementLine(String statementInfo) {
		String[] info = statementInfo.split(";");

		// date
		String[] DateInfo = info[0].trim().split("-");
		int day = Integer.parseInt(DateInfo[0]);
		int month = Integer.parseInt(DateInfo[1]);
		int year = Integer.parseInt(DateInfo[2]);
		Date date = new Date(day, month, year);

		// valueDate
		String[] valueDateInfo = info[1].trim().split("-");
		int dayValue = Integer.parseInt(valueDateInfo[0]);
		int monthValue = Integer.parseInt(valueDateInfo[1]);
		int yearValue = Integer.parseInt(valueDateInfo[2]);
		Date valueDate = new Date(dayValue, monthValue, yearValue);

		String description = info[2].trim();
		double draft = 0.0;
		if (!info[3].isEmpty()) {
			draft = Double.parseDouble(info[3].trim());
		}
		double credit = 0.0;
		if (!info[4].isEmpty()) {
			credit = Double.parseDouble(info[4].trim());
		}
		double accountingBalance = 0.0;
		if (!info[5].isEmpty()) {
			accountingBalance = Double.parseDouble(info[5].trim());
		}
		double availableBalance = 0.0;
		if (!info[6].isEmpty()) {
			availableBalance = Double.parseDouble(info[6].trim());
		}

		Category category = null;
		if (info.length == 8) {
			category = new Category(info[7].trim());
		}

		return new StatementLine(date, valueDate, description, draft, credit, accountingBalance, availableBalance, category);
	}

	public StatementLine(Date date, Date valueDate, String description, double draft, double credit, double accountingBalance, double availableBalance, Category category) {
		if (date == null || valueDate == null) {
			throw new IllegalArgumentException("Date and valueDate cannot be null");
		}
		if (description == null || description.isEmpty()) {
			throw new IllegalArgumentException("Description cannot be null or empty");
		}
		if (draft > 0) {
			throw new IllegalArgumentException("Draft amount cannot be positive");
		}
		if (credit < 0) {
			throw new IllegalArgumentException("Credit amount cannot be negative");
		}

		this.date = date;
		this.valueDate = valueDate;
		this.description = description;
		this.draft = draft;
		this.credit = credit;
		this.accountingBalance = accountingBalance;
		this.availableBalance = availableBalance;
		this.category = category;
	}

	public Date getDate() {
		return date;
	}

	public Date getValueDate() {
		return valueDate;
	}

	public String getDescription() {
		return description;
	}

	public double getCredit() {
		return credit;
	}

	public double getDraft() {
		return draft;
	}

	public double getAccountingBalance() {
		return accountingBalance;
	}

	public double getAvailableBalance() {
		return availableBalance;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category cat) {
		this.category = cat;
	}

	@Override
	public int compareTo(StatementLine o) {
		return this.getDate().compareTo(o.getDate());
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof StatementLine)) {
			return false;
		}
		StatementLine o = (StatementLine) obj;
		return this.date.equals(o.getDate()) &&
				this.valueDate.equals(o.getValueDate()) &&
				this.description.equals(o.getDescription()) &&
				this.draft == o.getDraft() &&
				this.credit == o.getCredit() &&
				this.accountingBalance == o.getAccountingBalance() &&
				this.availableBalance == o.getAvailableBalance();
	}
}
