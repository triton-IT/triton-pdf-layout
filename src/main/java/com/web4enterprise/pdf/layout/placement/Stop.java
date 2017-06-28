/*
 * Copyright 2017 web4enterprise.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.web4enterprise.pdf.layout.placement;

/**
 * Defines a stop on an element.
 * 
 * 
 * @author Régis Ramillien
 */
public class Stop {
	/**
	 * The type of stop.
	 */
	StopType type;
	/**
	 * The position on the stop on element.
	 */
	float position;
	
	/**
	 * Create a stop from the type.
	 * 
	 * @param type The type of stop.
	 * @param position The position of stop on element.
	 */
	public Stop(StopType type, float position) {
		this.type = type;
		this.position = position;
	}

	/**
	 * Get the type of stop.
	 * 
	 * @return The type.
	 */
	public StopType getType() {
		return type;
	}

	/**
	 * Set the type of stop.
	 * 
	 * @param type The type to set.
	 */
	public void setType(StopType type) {
		this.type = type;
	}

	/**
	 * Get the position of stop.
	 * 
	 * @return The position.
	 */
	public float getPosition() {
		return position;
	}

	/**
	 * Set the position of stop.
	 * 
	 * @param position The position to set.
	 */
	public void setPosition(float position) {
		this.position = position;
	}
	
	
}
