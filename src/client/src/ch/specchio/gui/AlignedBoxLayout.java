package ch.specchio.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.BoxLayout;

/**
 * A BoxLayout that aligns its sub-components on an off-centre axis.
 */
public class AlignedBoxLayout extends BoxLayout {
	
	/** serialisation version identifier */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param container	the container to be laid out
	 * @param axis		the axis along which to lay out the components
	 */
	public AlignedBoxLayout(Container container, int axis) {
		
		super(container, axis);
		
	}
	
	
	/**
	 * Compute the size of a container and the position of the alignment axis
	 * 
	 * @param container	the container
	 * 
	 * @return a new AlignedBoxLayoutBounds object
	 */
	private AlignedBoxLayoutBounds getBounds(Container container) {
		
		AlignedBoxLayoutBounds bounds = new AlignedBoxLayoutBounds();
		for (Component c : container.getComponents()) {
			if (c.isVisible()) {
				bounds.addComponent(c);
			}
		}
		
		return bounds;
		
	}
	
	
	/**
	 * Lay out a container.
	 * 
	 * @param container	the container to be laid out
	 */
	public void layoutContainer(Container container) {
		
		// perform normal box layout
		super.layoutContainer(container);
		
		// compute the bounds for the container
		AlignedBoxLayoutBounds bounds = getBounds(container);
		
		// align the sub-components
		for (Component c : container.getComponents()) {
			if (c.isVisible()) {
				if (c instanceof AlignedBox) {
					AlignedBox box = (AlignedBox)c;
					Point currentLocation = c.getLocation();
					Dimension dim = c.getPreferredSize();
					if (getAxis() == X_AXIS) {
						c.setBounds(currentLocation.x, bounds.getAlignmentPosition() - box.getYAlignmentPosition(), dim.width, dim.height);
					} else if (getAxis() == Y_AXIS) {
						c.setBounds(bounds.getAlignmentPosition() - box.getXAlignmentPosition(), currentLocation.y, dim.width, dim.height);
					}
				}
			}
		}
		
	}
	
	
	/**
	 * Compute the preferred size of a container, given the components it contains
	 * 
	 * @param container	the container
	 * 
	 * @return the preferred size of the container
	 */
	public Dimension preferredLayoutSize(Container container) {
		
		return preferredLayoutSize(container, getBounds(container));
		
	}
	
	
	/**
	 * Compute the preferred size of a container, given the bounds computed for it.
	 * 
	 * @param container	the container
	 * @param bounds	the pre-computed bounds
	 */
	private Dimension preferredLayoutSize(Container container, AlignedBoxLayoutBounds bounds) {
		
		// the preferred size is the bounds size plus insets
		Insets insets = container.getInsets();
		Dimension dim = new Dimension(
				bounds.getWidth() + insets.left + insets.right,
				bounds.getHeight() + insets.top + insets.bottom
			);
		
		return dim;
		
	}
		
		
	
	
	/**
	 * A convenience structure for storing all of the information required to
	 * perform layout.
	 */
	private class AlignedBoxLayoutBounds {
		
		/** the dimensions of the container */
		private Dimension dim;
		
		/** the location of the axis of alignment */
		private int alignmentPosition;
		
		/**
		 * Constructor.
		 * 
		 */
		public AlignedBoxLayoutBounds() {
			
			// start with zero sizes
			dim = new Dimension(0, 0);
			alignmentPosition = 0;
			
		}
		
		
		/**
		 * Stretch the structure to accomodate another component.
		 *
		 * @param c	the component
		 */
		public void addComponent(Component c) {

			// get the dimensions of the new component
			Dimension componentDimensions = c.getPreferredSize();
			
			if (getAxis() == X_AXIS) {
				
				// increase width to accomodate the new component
				dim.width += componentDimensions.width;
				
				// make sure the alignment position is down-ward enough to align the new component
				if (c instanceof AlignedBox) {
					alignmentPosition = Math.max(((AlignedBox)c).getYAlignmentPosition(), alignmentPosition);
				}
				
				// make sure we have enough height to accomodate the new component
				dim.height = Math.max(alignmentPosition + componentDimensions.height, dim.height);
				
			} else if (getAxis() == Y_AXIS) {
				
				// increase height to accomodate the new component
				dim.height += componentDimensions.height;
				
				// make sure the alignment position is right-ward enough to align the new component
				if (c instanceof AlignedBox) {
					alignmentPosition = Math.max(((AlignedBox)c).getXAlignmentPosition(), alignmentPosition);
				}
				
				// make sure we have enough width to accomodate the new component
				dim.width = Math.max(alignmentPosition + componentDimensions.width, dim.width);
			
			}
			
		}
		
		
		/**
		 * Get the alignment position.
		 * 
		 * @return the distance from the top (X_AXIS) or left (Y_AXIS) to the axis of alignment
		 */
		public int getAlignmentPosition() {
			
			return alignmentPosition;
			
		}
		
		
		/**
		 * Get the height of the layout.
		 * 
		 * @return the height required to accomodate all components
		 */
		public int getHeight() {
			
			return dim.height;
			
		}
		
		
		/**
		 * Get the width of the layout.
		 * 
		 * @return the width required to accomodate all components
		 */
		public int getWidth() {
			
			return dim.width;
			
		}
		
	}
	
	
	
	/**
	 * Interface to be implemented by components that will be aligned using this
	 * layout manager.
	 */
	public interface AlignedBox {
		
		/**
		 * Get the horizontal alignment position. Used for BoxLayout.Y_AXIS orientations.
		 * 
		 * @return the distance in pixels from the top edge of the component to the alignment position
		 */
		public int getXAlignmentPosition();
		
		/**
		 * Get the vertical alignment position. Used for BoxLayout.X_AXIS orientations
		 * 
		 * @return the distance in pixels from the left edge of the component to the alignment position
		 */
		public int getYAlignmentPosition();	
		
	}

}
