package com.redcup.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class Participant implements Serializable, 
	Comparable<Participant> {
	private static final long serialVersionUID = 1L;

	public class OnNameChangedEvent {
		private final Participant source;

		public OnNameChangedEvent(Participant source) {
			this.source = source;
		}

		public Participant getSource() {
			return this.source;
		}
	}

	public interface OnNameChangedListener {
		public void onNameChanged(OnNameChangedEvent event);
	}

	private String name;
	private int id;

	private final Collection<OnNameChangedListener> onNameChangedListeners = new ArrayList<Participant.OnNameChangedListener>();

	public Participant(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
		this.raiseOnNameChangedEvent(new OnNameChangedEvent(this));
	}

	public String getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Participant another) {
		return new CompareToBuilder()
			.append(this.name, another.name)
			.toComparison();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o == this) {
			return true;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		Participant rhs = (Participant) o;
		return new EqualsBuilder()
			.append(this.id, rhs.id)
			.append(this.name, rhs.name)
			.isEquals();
	}

	public void addOnNameChangedListener(OnNameChangedListener listener) {
		if (!this.onNameChangedListeners.contains(listener)) {
			this.onNameChangedListeners.add(listener);
		}
	}

	public void removeOnNameChangedListener(OnNameChangedListener listener) {
		this.onNameChangedListeners.remove(listener);
	}

	private void raiseOnNameChangedEvent(OnNameChangedEvent event) {
		for (OnNameChangedListener l : this.onNameChangedListeners) {
			l.onNameChanged(event);
		}
	}
}
