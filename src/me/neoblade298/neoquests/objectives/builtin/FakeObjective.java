package me.neoblade298.neoquests.objectives.builtin;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;

public class FakeObjective extends Objective {
	private String line;
	
	public FakeObjective() {
		super();
		this.needed = 0;
	}

	public FakeObjective(LineConfig cfg) {
		super(null, cfg);
		this.needed = cfg.getInt("needed", 0);
		this.line = cfg.getLine();
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new FakeObjective(cfg);
	}

	@Override
	public String getKey() {
		return "fake";
	}

	@Override
	public String getDisplay() {
		return line;
	}
}
