package communicationChannel;

/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Copyright: 2017
 *      Author: Pr. Olivier Gruber <olivier dot gruber at acm dot org>
 */

public class CircularBuffer {
	  volatile int m_tail, m_head;
	  volatile byte m_bytes[];

	  public CircularBuffer(int capacity) {
	    m_bytes = new byte[capacity];
	    m_tail = m_head = 0;
	  }

	  /**
	   * @return true if this buffer is full, false otherwise
	   */
	  public synchronized  boolean full() {
	    int next = (m_head + 1) % m_bytes.length;
	    return (next == m_tail);
	  }

	  /**
	   * @return true if this buffer is empty, false otherwise
	   */
	  public synchronized  boolean empty() {
	    return (m_tail == m_head);
	  }

	  /**
	   * @param b: the byte to push in the buffer
	   * @return the next available byte
	   * @throws an IllegalStateException if full.
	   */
	  public synchronized  void push(byte b) {
	    int next = (m_head + 1) % m_bytes.length;
	    if (next == m_tail)
	      throw new IllegalStateException();
	    m_bytes[m_head] = b;
	    m_head = next;
	  }

	  /**
	   * @return the next available byte
	   * @throws an IllegalStateException if empty.
	   */
	  public synchronized  byte pull() {
	    if (m_tail == m_head)
	      throw new IllegalStateException();
	    int next = (m_tail + 1) % m_bytes.length;
	    byte bits = m_bytes[m_tail];
	    m_tail = next;
	    return bits;
	  }

	}