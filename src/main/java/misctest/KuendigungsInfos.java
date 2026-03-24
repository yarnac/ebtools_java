package misctest;

import java.sql.Date;

public class KuendigungsInfos {
	private Date kuendigungsDatum;
	private Double strike;
	private Double delta;
	private Double barwert;

	public Date getKuendigungsDatum() {
		return kuendigungsDatum;
	}

	public void setKuendigungsDatum(Date kuendigungsDatum) {
		this.kuendigungsDatum = kuendigungsDatum;
	}

	public Double getStrike() {
		return strike;
	}

	public void setStrike(Double strike) {
		this.strike = strike;
	}

	public Double getDelta() {
		return delta;
	}

	public void setDelta(Double delta) {
		this.delta = delta;
	}

	public Double getBarwert() {
		return barwert;
	}

	public void setBarwert(Double barwert) {
		this.barwert = barwert;
	}
}
