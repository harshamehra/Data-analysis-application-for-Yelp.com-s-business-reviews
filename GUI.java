mport java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class GUI {
	public static void main(String arg[]) {
		GUIManager.createGUI();
	}

	public static class GUIManager {
		static JFrame mainFrame;
		static JPanel mainCategoryListPanel = new JPanel();
		static JPanel subCategoryListPanel = new JPanel();
		static JPanel attributeListPanel = new JPanel();
		
		
		static JTable resultTable;
		static DefaultTableModel resultDatatable;
		
		static JComboBox<String> locationDropDown;
		static JComboBox<String> dayDropDown;
		static JComboBox<String> fromDropDown;
		static JComboBox<String> toDropDown;
		static JComboBox<String> searchDropDown;
		
		public static final int MAIN_CATEGORY_LIST = 0;
		public static final int SUB_CATEGORY_LIST = 1;
		public static final int ATTRIBUTE_LIST = 2;
		
		public static void createGUI() {
			mainFrame = new JFrame();
			
			//
			addComponentsToPane(mainFrame.getContentPane());
			//Show Window
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.setTitle("Homework 3 : Aastha Rajput");
			mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			mainFrame.setVisible(true);
		
			//Reset attribute list
			GUIManager.setMainCategory(DBManager.getAllMainCategories());
			GUIManager.setSubCategory(null);
			GUIManager.setAttributes(null);
			GUIManager.setLocation(null);
			GUIManager.setDayOfWeek(null);
			GUIManager.setFromHours(null, null);
			GUIManager.setToHours(null, null);
		}
		
		public static void showReviewFrame(String bid) {
			JFrame reviewFrame = new JFrame();
			
			JPanel reviewResultPanel = new JPanel();
			
			String columns[] = {"Review Date","Stars","Review Text","UserID","Useful Votes","Funny Votes","Cool Votes"};
			
			JTable reviewTable = new JTable();
			DefaultTableModel reviewDatatable = new DefaultTableModel();
			
			for(String columnName : columns)
				reviewDatatable.addColumn(columnName);
			reviewTable.setModel(reviewDatatable);
			
			List<List<String>> reviewList = DBManager.getReviews(bid); 
			for(List<String> row : reviewList) {
				reviewDatatable.addRow(row.toArray());
			}
			
			JScrollPane sp = new JScrollPane(reviewTable);
			
			reviewResultPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1;
			c.weighty = 1;
			reviewResultPanel.add(sp,c);
			reviewFrame.add(reviewResultPanel,BorderLayout.CENTER);
			reviewFrame.setTitle("Reviews : " + bid);
			reviewFrame.setSize(800,600);
			reviewFrame.setVisible(true);
		}
		
		public static void addComponentsToPane(Container pane) {
			//Default Layout is Border Layout
			
			//Grid Bag Layout Constraints
			GridBagConstraints c = new GridBagConstraints();
			
			//Create Main JPanel
			JPanel mainPanel = new JPanel();
			
			JPanel topPanel = createTopPanel();
			JPanel bottomPanel = createBottomPanel();
			
			//Panel Layout to GridLayout
			mainPanel.setLayout(new GridBagLayout());
			
			c.fill = GridBagConstraints.BOTH;
			c.gridx = 0;
			c.gridy = 0;
			c.weighty = 0.9;	//80% of Height
			c.weightx = 1;
			mainPanel.add(topPanel,c);
			
			c.fill = GridBagConstraints.BOTH;
			c.weighty = 0.1;	//20% of Height
			c.gridx = 0;
			c.gridy = 1;
			mainPanel.add(bottomPanel,c);
			
			pane.add(mainPanel,BorderLayout.CENTER);
		}
		
		public static JPanel createTopPanel() {
				//Create Top Panel
				JPanel topPanel = new JPanel();
				//topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				topPanel.setLayout(new GridBagLayout());
				
				//Main Category List
				JPanel mainCategoryPanel = GUIManager.createListPanel(GUIManager.MAIN_CATEGORY_LIST);
				
				//Sub Category List
				JPanel subCategoryPanel = GUIManager.createListPanel(GUIManager.SUB_CATEGORY_LIST);
				
				//Attribute List
				JPanel attributePanel = GUIManager.createListPanel(GUIManager.ATTRIBUTE_LIST);
				
				//Business Result List
				JPanel businessResultPanel = new JPanel();
				JLabel businessResultLabel = new JLabel("Business Result");
				businessResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
				
				String columns[] = {"","Business Id","Name","Address","City","State","Stars","# of Reviews","# of Checkins"};
				
				resultTable = new JTable();
				resultTable.addMouseListener(GUIController.tableMouseListener);
				resultDatatable = new DefaultTableModel() {
					private static final long serialVersionUID = 1L;

					@Override
				    public boolean isCellEditable(int row, int column) {
				       //all cells false
				       return false;
				    }
				};
				for(String columnName : columns)
					resultDatatable.addColumn(columnName);
				resultTable.setModel(resultDatatable);
				JScrollPane sp = new JScrollPane(resultTable);
				
				businessResultPanel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.fill = GridBagConstraints.HORIZONTAL;
				c.weightx = 1;
				businessResultPanel.add(businessResultLabel,c);
				
				c.gridx = 0;
				c.gridy = 1;
				c.fill = GridBagConstraints.BOTH;
				c.weighty = 1;
				businessResultPanel.add(sp,c);
				
				//businessResultPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
				
				
				c.gridx = 0;
				c.gridy = 0;
				c.fill = GridBagConstraints.BOTH;
				c.weighty = 1;
				c.weightx = 0.15;	//33% of 50% Screen Width
				topPanel.add(mainCategoryPanel,c);
				c.gridx = 1;
				c.weightx = 0.15;	//33% of 50% Screen Width
				topPanel.add(subCategoryPanel,c);
				c.gridx = 2;
				c.weightx = 0.15;	//33% of 50% Screen Width
				topPanel.add(attributePanel,c);
				c.gridx = 3;
				c.weightx = 0.55;
				topPanel.add(businessResultPanel,c);
				
				return topPanel;
		}
		
		public static JPanel createBottomPanel() {
			JPanel bottomPanel = new JPanel();
			GridBagConstraints c = new GridBagConstraints();
			
			//bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
			bottomPanel.setLayout(new GridBagLayout());
			
			//Add Location Drop Down
			JPanel locationPanel = new JPanel();
			
			String[] p = {"Any"};
			locationDropDown = new JComboBox<String>(p);
			locationDropDown.addActionListener(GUIController.locationActionListener);
			JLabel locationLabel = new JLabel("Location");
			
			locationPanel.setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.4;
			locationPanel.add(locationLabel,c);
			c.weightx = 0.6;
			c.gridy = 1;
			locationPanel.add(locationDropDown,c);
			
			
			//Add Day Drop Down
			JPanel dayPanel = new JPanel();
			
			dayDropDown = new JComboBox<String>(p);
			dayDropDown.addActionListener(GUIController.dayActionListener);
			JLabel dayLabel = new JLabel("Day of the Week");
			
			dayPanel.setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.4;
			dayPanel.add(dayLabel,c);
			c.weightx = 0.6;
			c.gridy = 1;
			dayPanel.add(dayDropDown,c);
			
			//Add From Drop Down
			JPanel fromPanel = new JPanel();
			
			fromDropDown = new JComboBox<String>(p);
			fromDropDown.addActionListener(GUIController.fromActionListener);
			JLabel fromLabel = new JLabel("From");
			
			fromPanel.setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.4;
			fromPanel.add(fromLabel,c);
			c.weightx = 0.6;
			c.gridy = 1;
			fromPanel.add(fromDropDown,c);
			
			//Add To Drop Down
			JPanel toPanel = new JPanel();
			
			toDropDown = new JComboBox<String>(p);
			toDropDown.addActionListener(GUIController.toActionListener);
			JLabel toLabel = new JLabel("To");
			
			toPanel.setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.4;
			toPanel.add(toLabel,c);
			c.weightx = 0.6;
			c.gridy = 1;
			toPanel.add(toDropDown,c);
			
			//Add Search For Drop Down
			JPanel searchPanel = new JPanel();
			
			String[] searchOptions = {"Any Attribute","All Attributes"};
			searchDropDown = new JComboBox<String>(searchOptions);
			searchDropDown.addActionListener(GUIController.searchForActionListener);
			JLabel searchLabel = new JLabel("Search For");
			
			searchPanel.setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.4;
			searchPanel.add(searchLabel,c);
			c.weightx = 0.6;
			c.gridy = 1;
			searchPanel.add(searchDropDown,c);
			
			//Add Search Button
			JButton search = new JButton("Search");
			search.addActionListener(GUIController.searchButtonActionListener);
			bottomPanel.setLayout(new GridBagLayout());
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.fill = GridBagConstraints.NONE;
			c.ipadx = 150;
			bottomPanel.add(locationPanel,c);
			//Add Day Drop Down
			c.gridx = 1;
			bottomPanel.add(dayPanel,c);
			c.gridx = 2;
			bottomPanel.add(fromPanel,c);
			c.gridx = 3;
			bottomPanel.add(toPanel,c);
			c.gridx = 4;
			bottomPanel.add(searchPanel,c);
			c.gridx = 5;
			c.fill = GridBagConstraints.VERTICAL;
			bottomPanel.add(search,c);
			
			return bottomPanel;
		}
		
		public static JPanel createListPanel(int type) {
			//Create Components
			
			//1. Heading
			JLabel panelHeading = new JLabel();
			if(type == MAIN_CATEGORY_LIST) panelHeading.setText("Main Categories");
			else if(type == SUB_CATEGORY_LIST) panelHeading.setText("Sub Categories");
			else if(type == ATTRIBUTE_LIST) panelHeading.setText("Attributes");
			panelHeading.setHorizontalAlignment(SwingConstants.CENTER);
			
			//2. List of Categories
			JPanel panelList = null;
			
			if(type == MAIN_CATEGORY_LIST)
				panelList = mainCategoryListPanel;
			else if(type == SUB_CATEGORY_LIST)
				panelList = subCategoryListPanel;
			else if(type == ATTRIBUTE_LIST)
				panelList = attributeListPanel;
			JScrollPane listScrollPanel = new JScrollPane(panelList);
			panelList.setBackground(Color.WHITE);
			panelList.setLayout(new BoxLayout(panelList,  BoxLayout.PAGE_AXIS));
			
			//Main Category Panel
			JPanel panel = new JPanel();
			//panel.setBorder(BorderFactory.createLineBorder(Color.RED));
			panel.setLayout(new GridBagLayout());
			
			//Put Components together
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1;
			
			panel.add(panelHeading,c); //Heading
			c.gridx = 0;
			c.gridy = 1;
			c.fill = GridBagConstraints.BOTH;
			c.weighty = 1;
			panel.add(listScrollPanel,c); //List
			
			return panel;
		}
		public static JPanel createMainCategoryPanel() {
			return null;
		}
		
		public static void setMainCategory(List<String> mainCategories) {
			mainCategoryListPanel.removeAll();
			
			for(String option : mainCategories) {
				JCheckBox temp = new JCheckBox(option);
				temp.setMaximumSize(new Dimension(Short.MAX_VALUE,25));
				temp.setBackground(Color.WHITE);
				temp.addItemListener(GUIController.mainCategoryCBListener);
				mainCategoryListPanel.add(temp);
			}
			
			mainCategoryListPanel.revalidate();
			mainCategoryListPanel.repaint();
		}
		
		public static void setSubCategory(List<String> subCategories) {
			subCategoryListPanel.removeAll();
			
			if(subCategories != null) {
				for(String option : subCategories) {
					JCheckBox temp = new JCheckBox(option);
					temp.setMaximumSize(new Dimension(Short.MAX_VALUE,25));
					temp.setBackground(Color.WHITE);
					temp.addItemListener(GUIController.subCategoryCBListener);
					subCategoryListPanel.add(temp);
				}
			}
			
			subCategoryListPanel.revalidate();
			subCategoryListPanel.repaint();
		}
		
		public static void setAttributes(List<String> attributeList) {
			attributeListPanel.removeAll();
			
			if(attributeList != null) {
				for(String option : attributeList) {
					JCheckBox temp = new JCheckBox(option);
					temp.setMaximumSize(new Dimension(Short.MAX_VALUE,25));
					temp.setBackground(Color.WHITE);
					temp.addItemListener(GUIController.attributeCBListener);
					attributeListPanel.add(temp);
				}
			}
			
			attributeListPanel.revalidate();
			attributeListPanel.repaint();
		}
		public static void setResultTable(List<List<String>> businessList) {
			
			//Remove All Rows
			while(resultDatatable.getRowCount() > 0)
				resultDatatable.removeRow(0);
			
			for(List<String> row : businessList) {
				resultDatatable.addRow(row.toArray());
			}
			
			resultTable.revalidate();
			resultTable.repaint();
		}
		
		public static void setLocation(List<String> locations) {
			locationDropDown.removeAllItems();
			locationDropDown.addItem("Any");
			locationDropDown.setSelectedItem("Any");
			if(locations != null) {
				for(String location : locations) {
					locationDropDown.addItem(location);
				}
			}
			locationDropDown.revalidate();
			locationDropDown.repaint();
		}
		
		public static void setDayOfWeek(List<String> days) {
			dayDropDown.removeAllItems();
			dayDropDown.addItem("Any");
			dayDropDown.setSelectedItem("Any");
			System.out.println("Setting days");
			if(days!=null) {
				for(String day : days) {
					dayDropDown.addItem(day);
				}
			}
			dayDropDown.revalidate();
			dayDropDown.repaint();
		}
		
		public static void setFromHours(Integer minRange, Integer maxRange) {
			fromDropDown.removeAllItems();
			fromDropDown.addItem("Any");
			fromDropDown.setSelectedItem("Any");
			if(minRange != null && maxRange != null) {
				int minHour = Math.floorDiv(minRange, 60);
				int maxHour = Math.floorDiv(maxRange, 60) + 1;
				for(int i = minHour; i <= maxHour; i++) {
					if(i<24) {
						fromDropDown.addItem(i + ":00");
					}
				}
			}
			fromDropDown.revalidate();
			fromDropDown.repaint();
		}
		
		public static void setToHours(Integer minRange, Integer maxRange) {
			toDropDown.removeAllItems();
			toDropDown.addItem("Any");
			toDropDown.setSelectedItem("Any");
			if(minRange != null && maxRange != null) {
				int minHour = Math.floorDiv(minRange, 60);
				int maxHour = Math.floorDiv(maxRange, 60) + 1;
				for(int i = minHour; i <= maxHour; i++) {
					if(i<24) {
						toDropDown.addItem(i + ":00");
					}
				}
			}
			toDropDown.revalidate();
			toDropDown.repaint();
		}
	}
	
	public static class GUIController {
		public static List<String> selectedMainCategories = new ArrayList<String>();
		public static List<String> selectedSubCategories = new ArrayList<String>();
		public static List<String> selectedAttributes = new ArrayList<String>();
		public static Map<Integer, Map<String, Integer>> dayMap = new HashMap<Integer, Map<String, Integer>>();

		public static String construct = "or";
		public static String location = null;
		public static Integer dayOfWeek = null;
		public static Integer fromHour = null;
		public static Integer toHour = null;
		
		//Main Category Item Listener
		public static ItemListener mainCategoryCBListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox jCB = (JCheckBox)e.getSource();
				//Get the checked/unchecked state of check box
				if(e.getStateChange() == ItemEvent.DESELECTED) {
					selectedMainCategories.remove(jCB.getText());
				} else if(e.getStateChange() == ItemEvent.SELECTED) {
					selectedMainCategories.add(jCB.getText());
				}
				
				//Update subQueryList
				List<String> subCategoriesList = DBManager.getAllSubCategories(selectedMainCategories, construct);
				selectedSubCategories.clear();
				selectedAttributes.clear();
				dayMap.clear();
				location = null;
				dayOfWeek = null;
				fromHour = null;
				toHour = null;
				
				GUIManager.setSubCategory(subCategoriesList);
				//Reset attribute list
				GUIManager.setAttributes(null);
				GUIManager.setLocation(null);
				GUIManager.setDayOfWeek(null);
				GUIManager.setFromHours(null, null);
				GUIManager.setToHours(null, null);
				
			}
		};
		
		//Sub Category Item Listener
		public static ItemListener subCategoryCBListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox jCB = (JCheckBox)e.getSource();
				//Get the checked/unchecked state of check box
				if(e.getStateChange() == ItemEvent.DESELECTED) {
					selectedSubCategories.remove(jCB.getText());
				} else if(e.getStateChange() == ItemEvent.SELECTED) {
					selectedSubCategories.add(jCB.getText());
				}
				
				//Update subQueryList
				List<String> attributeList = DBManager.getAllAttributes(selectedMainCategories,selectedSubCategories, construct);
				selectedAttributes.clear();
				dayMap.clear();
				location = null;
				dayOfWeek = null;
				fromHour = null;
				toHour = null;
				
				GUIManager.setAttributes(attributeList);
				GUIManager.setLocation(null);
				GUIManager.setDayOfWeek(null);
				GUIManager.setFromHours(null, null);
				GUIManager.setToHours(null, null);
			}
		};
		
		//Attribute Item Listener
		public static ItemListener attributeCBListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox jCB = (JCheckBox)e.getSource();
				//Get the checked/unchecked state of check box
				if(e.getStateChange() == ItemEvent.DESELECTED) {
					selectedAttributes.remove(jCB.getText());
				} else if(e.getStateChange() == ItemEvent.SELECTED) {
					selectedAttributes.add(jCB.getText());
				}
				
				//Update location
				List<String> locationList = DBManager.getAllLocations(selectedMainCategories,selectedSubCategories,selectedAttributes,construct);
				location = null;
				GUIManager.setLocation(locationList);
				
				dayMap.clear();
				dayMap = DBManager.getAllDays(selectedMainCategories,selectedSubCategories,selectedAttributes,location,construct);
				dayOfWeek = null;
				List<String> dayList = new ArrayList<String>();
				List<Integer> days = new ArrayList<Integer>();
				days.addAll(dayMap.keySet());
				System.out.println("Attribute: Setting Days" + days);
				days.sort(null);
				for(Integer d : days) {
					String day = "";
					if(d==0) day = "Sunday";
					else if(d==1) day = "Monday";
					else if(d==2) day = "Tuesday";
					else if(d==3) day = "Wednesday";
					else if(d==4) day = "Thursday";
					else if(d==5) day = "Friday";
					else if(d==6) day = "Saturday";
					dayList.add(day);
				}
				System.out.println("Attribute: Setting Days" + dayList);
				GUIManager.setDayOfWeek(null);
				GUIManager.setDayOfWeek(dayList);
				
				dayOfWeek = null;
				fromHour = null;
				toHour = null;
				
				GUIManager.setFromHours(null, null);
				GUIManager.setToHours(null, null);
			}
		};
		
		//Location Action Listener
		public static ActionListener locationActionListener = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox<?> jCB = (JComboBox<?>) e.getSource();
					int selectedIndex = jCB.getSelectedIndex();
					if(selectedIndex == 0) {
						location = null;
					} else {
						location = (String)jCB.getItemAt(selectedIndex);
					}
					System.out.println("Location" + location);
					//Update location
					
					dayMap.clear();
					dayMap = DBManager.getAllDays(selectedMainCategories,selectedSubCategories,selectedAttributes,location,construct);
					dayOfWeek = null;
					List<String> dayList = new ArrayList<String>();
					List<Integer> days = new ArrayList<Integer>();
					days.addAll(dayMap.keySet());
					System.out.println("Location: Setting Days" + days);
					days.sort(null);
					for(Integer d : days) {
						String day = "";
						if(d==0) day = "Sunday";
						else if(d==1) day = "Monday";
						else if(d==2) day = "Tuesday";
						else if(d==3) day = "Wednesday";
						else if(d==4) day = "Thursday";
						else if(d==5) day = "Friday";
						else if(d==6) day = "Saturday";
						dayList.add(day);
					}
					System.out.println("Location: Setting Days" + dayList);
					GUIManager.setDayOfWeek(dayList);

					fromHour = null;
					toHour = null;
					
					GUIManager.setFromHours(null, null);
					GUIManager.setToHours(null, null);
				}
		};
		
		//Day Action Listener
		public static ActionListener dayActionListener = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox<?> jCB = (JComboBox<?>)e.getSource();
					int selectedIndex = jCB.getSelectedIndex();
					if(selectedIndex == 0) {
						dayOfWeek = null;
					} else {
						String day = (String)jCB.getItemAt(selectedIndex);
						if(day != null) {
							if(day.equalsIgnoreCase("Sunday")) dayOfWeek = 0;
							else if(day.equalsIgnoreCase("Monday")) dayOfWeek = 1;
							else if(day.equalsIgnoreCase("Tuesday")) dayOfWeek = 2;
							else if(day.equalsIgnoreCase("Wednesday")) dayOfWeek = 3;
							else if(day.equalsIgnoreCase("Thursday")) dayOfWeek = 4;
							else if(day.equalsIgnoreCase("Friday")) dayOfWeek = 5;
							else if(day.equalsIgnoreCase("Saturday")) dayOfWeek = 6;
							else dayOfWeek = null;
						} else {
							dayOfWeek = null;
						}
						
					}
					
					//Update location
					fromHour = null;
					toHour = null;
					if(dayOfWeek == null) {
						fromHour = null;
						toHour = null;
						
						GUIManager.setFromHours(null, null);
						GUIManager.setToHours(null, null);
					} else {
						if(dayMap.containsKey(dayOfWeek)) {
							Integer minHour = dayMap.get(dayOfWeek).get("from");
							Integer maxHour = dayMap.get(dayOfWeek).get("to");
							
							GUIManager.setFromHours(minHour, maxHour);
							GUIManager.setToHours(minHour, maxHour);
						}
					}
				}
			};
			
			//Day Action Listener
			public static ActionListener fromActionListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox<?> jCB = (JComboBox<?>)e.getSource();
					int selectedIndex = jCB.getSelectedIndex();
					if(selectedIndex == 0) {
						fromHour = null;
						//System.out.print("From " + fromHour);
					} else {
						String fromH = (String)jCB.getItemAt(selectedIndex);
						if(fromH == null) {
							fromHour = null;
						} else {
							fromHour = Integer.valueOf(fromH.split(":")[0])*60;
							//System.out.print("From " + fromHour);
						}
					}
				}
			};
			
			//To Action Listener
			public static ActionListener toActionListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox<?> jCB = (JComboBox<?>)e.getSource();
					int selectedIndex = jCB.getSelectedIndex();
					if(selectedIndex == 0) {
						toHour = null;
						//System.out.print("To " + toHour);
					} else {
						String fromH = (String)jCB.getItemAt(selectedIndex);
						if(fromH == null)
							toHour = null;
						else {
							toHour = Integer.valueOf(fromH.split(":")[0])*60;
							//System.out.print("To " + toHour);
						}
					}
				}
			};
		
		//Search For Item Listener
		public static ActionListener searchForActionListener = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox<?> jCB = (JComboBox<?>)e.getSource();
					int selectedIndex = jCB.getSelectedIndex();
					if(selectedIndex == 0) {
						construct = "or";
					} else {
						construct = "and";
					}
					//Update subQueryList
					//List<String> attributeList = DBManager.getAllAttributes(selectedMainCategories,selectedSubCategories);
					//selectedAttributes.clear();
					//GUIManager.setAttributes(attributeList);
					
				}
			};
		
		//Search Button Listener
		public static ActionListener searchButtonActionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<List<String>> businessList = DBManager.getAllBusiness(selectedMainCategories,selectedSubCategories, selectedAttributes, location, dayOfWeek, fromHour, toHour, construct);
				GUIManager.setResultTable(businessList);
			}
			
		};
		
		public static MouseAdapter tableMouseListener = new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
		        JTable table =(JTable) mouseEvent.getSource();
		        Point point = mouseEvent.getPoint();
		        int row = table.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2) {
		        	String bid = (String)GUIManager.resultDatatable.getValueAt(row, 1);
		        	System.out.println("Showing Review for " + bid);
		        	GUIManager.showReviewFrame(bid);
		        }
		    }
		};
		
		
	}
	
	public static class DBManager {
		static Connection conn;
		
		//Database Credentials
		static String URL = "jdbc:oracle:thin:@localhost:1521:ORACLE";
		static String USER = "scott";
		static String PASSWORD = "tiger";
		
		public static void init() {
			System.out.println("Initializing Database");
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				System.out.println("Connecting with " + URL);
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
				System.out.println("Connection Successful");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		public static List<Map<String,Object>> executeQuery(String query){
			List<Map<String,Object>> resultSet = new ArrayList<Map<String,Object>>();
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					System.out.println(rs.getString(2));
					rs.next();
				}
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			return resultSet;
		}
		
		public static List<String> getAllMainCategories() {
			List<String> mainCategories = new ArrayList<String>();
			try {
				if(conn == null) init();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT Unique(cname) FROM BUSINESS_MAIN_CATEGORY ORDER BY cname");
				
				while(rs.next()) {
					mainCategories.add(rs.getString(1));
				}
			} catch(Exception e) {
				System.out.println("Error " + e);
				e.printStackTrace();
			}
			return mainCategories;
		}
		
		public static List<String> getAllSubCategories(List<String> mainCategories, String construct) {
			List<String> subCategories = new ArrayList<String>();
			if(mainCategories.isEmpty()) return subCategories;
			try {
				if(conn == null) init();
				List<String> pHolder = new ArrayList<String>();
				
				List<String> conditions = new ArrayList<String>();
				
				//Form query mainCategories - Mandatory
				int countMainCategories = mainCategories.size();
				pHolder.clear();
				while(pHolder.size() != countMainCategories) pHolder.add("?");
				
				String subQueryMainCat = "SELECT BID FROM BUSINESS_MAIN_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
				if(construct.equalsIgnoreCase("or")) {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID"; 
				} else {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countMainCategories;
				}
				conditions.add("(BID IN (" + subQueryMainCat + "))");
							
				//Final Query
				//"cname"
				String sqlQuery = "SELECT UNIQUE(CNAME) FROM BUSINESS_SUB_CATEGORY WHERE " +String.join(" AND ", conditions) + " ORDER BY CNAME";
				System.out.println("SQL QUERY : " + sqlQuery);
				PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
				int paramIndex = 1;
				for(String cat : mainCategories) {
					pstmt.setString(paramIndex, cat);
					paramIndex++;
				}
				
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()) {
					subCategories.add(rs.getString(1));
				}
			} catch(Exception e) {
				System.out.println("Error " + e);
				e.printStackTrace();
			}
			return subCategories;
		}
		
		public static List<String> getAllAttributes(List<String> mainCategories, List<String> subCategories, String construct) {
			List<String> attributes = new ArrayList<String>();
			if(mainCategories.isEmpty() || subCategories.isEmpty()) return attributes;
			try {
				if(conn == null) init();
				//SELECT unique(attribute) FROM BUSINESS_ATTRIBUTE 
				//WHERE BID IN 
				//			(SELECT BID FROM BUSINESS_MAIN_CATEGORY WHERE cname IN ('Restaurants'))
				//			AND
				//		BID IN
				//			(SELECT BID FROM BUSINESS_SUB_CATEGORY WHERE cname IN ('Car Wash'));
				
				//Prepare Query for Main Categories - OR Construct
				List<String> pHolder = new ArrayList<String>();
				
				List<String> conditions = new ArrayList<String>();
				
				//Form query mainCategories - Mandatory
				int countMainCategories = mainCategories.size();
				pHolder.clear();
				while(pHolder.size() != countMainCategories) pHolder.add("?");
				
				String subQueryMainCat = "SELECT BID FROM BUSINESS_MAIN_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
				if(construct.equalsIgnoreCase("or")) {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID"; 
				} else {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countMainCategories;
				}
				conditions.add("(BID IN (" + subQueryMainCat + "))");
				
				//SubCategories Optional with AND condition other condition
				if(subCategories != null && subCategories.size() > 0) {
					int countSubCategories = subCategories.size();
					pHolder.clear();
					while(pHolder.size() != countSubCategories) pHolder.add("?");
					
					String subQuerySubCat = "SELECT BID FROM BUSINESS_SUB_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
					if(construct.equalsIgnoreCase("or")) {
						subQuerySubCat = subQuerySubCat + " GROUP BY BID"; 
					} else {
						subQuerySubCat = subQuerySubCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countSubCategories;
					}
					conditions.add("(BID IN (" + subQuerySubCat + "))");
				}
				
				//Final Query
				//"Attribute"
				String sqlQuery = "SELECT UNIQUE(Attribute) FROM BUSINESS_ATTRIBUTE WHERE " +String.join(" AND ", conditions) + " ORDER BY Attribute";
				System.out.println("SQL QUERY : " + sqlQuery);
				PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
				int paramIndex = 1;
				for(String cat : mainCategories) {
					pstmt.setString(paramIndex, cat);
					paramIndex++;
				}
				if(subCategories != null) {
					for(String cat : subCategories) {
						pstmt.setString(paramIndex, cat);
						paramIndex++;
					}
				}
				
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()) {
					attributes.add(rs.getString(1));
				}
			} catch(Exception e) {
				System.out.println("Error " + e);
				e.printStackTrace();
			}
			return attributes;
		}
		
		public static List<String> getAllLocations(List<String> mainCategories, List<String> subCategories, List<String> attributes, String construct){
			List<String> locations = new ArrayList<String>();
			if(mainCategories.isEmpty()) return locations;
			try {
				List<String> pHolder = new ArrayList<String>();
				
				List<String> conditions = new ArrayList<String>();
				
				//Form query mainCategories - Mandatory
				int countMainCategories = mainCategories.size();
				pHolder.clear();
				while(pHolder.size() != countMainCategories) pHolder.add("?");
				
				String subQueryMainCat = "SELECT BID FROM BUSINESS_MAIN_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
				if(construct.equalsIgnoreCase("or")) {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID"; 
				} else {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countMainCategories;
				}
				conditions.add("(BID IN (" + subQueryMainCat + "))");
				
				//SubCategories Optional with AND condition other condition
				if(subCategories != null && subCategories.size() > 0) {
					int countSubCategories = subCategories.size();
					pHolder.clear();
					while(pHolder.size() != countSubCategories) pHolder.add("?");
					
					String subQuerySubCat = "SELECT BID FROM BUSINESS_SUB_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
					if(construct.equalsIgnoreCase("or")) {
						subQuerySubCat = subQuerySubCat + " GROUP BY BID"; 
					} else {
						subQuerySubCat = subQuerySubCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countSubCategories;
					}
					conditions.add("(BID IN (" + subQuerySubCat + "))");
				}
				
				//Attribute - Optional with AND condition other condition
				if(attributes != null && attributes.size() > 0) {
					int countAttributeCategories = attributes.size();
					pHolder.clear();
					while(pHolder.size() != countAttributeCategories) pHolder.add("?");
					
					String subQueryAttributeCat = "SELECT BID FROM BUSINESS_ATTRIBUTE WHERE ATTRIBUTE IN (" + String.join(",", pHolder) + ")";
					if(construct.equalsIgnoreCase("or")) {
						subQueryAttributeCat = subQueryAttributeCat + " GROUP BY BID"; 
					} else {
						subQueryAttributeCat = subQueryAttributeCat + " GROUP BY BID HAVING COUNT(ATTRIBUTE) = " + countAttributeCategories;
					}
					conditions.add("(BID IN (" + subQueryAttributeCat + "))");
				}
				
				//Final Query
				//"Business Id","Address","City","State","Stars","# of Reviews","# of Checkins"
				String sqlQuery = "SELECT UNIQUE(CITY), STATE FROM BUSINESS WHERE " +String.join(" AND ", conditions) + " ORDER BY STATE";
				System.out.println("SQL QUERY : " + sqlQuery);
				PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
				int paramIndex = 1;
				for(String cat : mainCategories) {
					pstmt.setString(paramIndex, cat);
					paramIndex++;
				}
				if(subCategories != null) {
					for(String cat : subCategories) {
						pstmt.setString(paramIndex, cat);
						paramIndex++;
					}
				}
				if(attributes != null) {
					for(String cat : attributes) {
						pstmt.setString(paramIndex, cat);
						paramIndex++;
					}
				}
				
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()) {
					String location = rs.getString(1) + "," + rs.getString(2);
					locations.add(location);
				}
	 			
			} catch (Exception e) {
				System.out.println("Get Location Error " + e);
			}
			
			return locations;
		}
		
		public static Map<Integer, Map<String, Integer>> getAllDays(List<String> mainCategories, List<String> subCategories, List<String> attributes, String location, String construct){
			Map<Integer, Map<String, Integer>> daysMap = new HashMap<Integer, Map<String, Integer>>();
			if(mainCategories.isEmpty()) return daysMap;
			try {
				List<String> pHolder = new ArrayList<String>();
				
				List<String> conditions = new ArrayList<String>();
				
				//Form query mainCategories - Mandatory
				int countMainCategories = mainCategories.size();
				pHolder.clear();
				while(pHolder.size() != countMainCategories) pHolder.add("?");
				
				String subQueryMainCat = "SELECT BID FROM BUSINESS_MAIN_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
				if(construct.equalsIgnoreCase("or")) {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID"; 
				} else {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countMainCategories;
				}
				conditions.add("(BID IN (" + subQueryMainCat + "))");
				
				//SubCategories Optional with AND condition other condition
				if(subCategories != null && subCategories.size() > 0) {
					int countSubCategories = subCategories.size();
					pHolder.clear();
					while(pHolder.size() != countSubCategories) pHolder.add("?");
					
					String subQuerySubCat = "SELECT BID FROM BUSINESS_SUB_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
					if(construct.equalsIgnoreCase("or")) {
						subQuerySubCat = subQuerySubCat + " GROUP BY BID"; 
					} else {
						subQuerySubCat = subQuerySubCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countSubCategories;
					}
					conditions.add("(BID IN (" + subQuerySubCat + "))");
				}
				
				//Attribute - Optional with AND condition other condition
				if(attributes != null && attributes.size() > 0) {
					int countAttributeCategories = attributes.size();
					pHolder.clear();
					while(pHolder.size() != countAttributeCategories) pHolder.add("?");
					
					String subQueryAttributeCat = "SELECT BID FROM BUSINESS_ATTRIBUTE WHERE ATTRIBUTE IN (" + String.join(",", pHolder) + ")";
					if(construct.equalsIgnoreCase("or")) {
						subQueryAttributeCat = subQueryAttributeCat + " GROUP BY BID"; 
					} else {
						subQueryAttributeCat = subQueryAttributeCat + " GROUP BY BID HAVING COUNT(ATTRIBUTE) = " + countAttributeCategories;
					}
					conditions.add("(BID IN (" + subQueryAttributeCat + "))");
				}
				
				//Location
				String state = null;
				String city = null;
				if(location != null) {
					String[] locArr = location.split(",");
					state = locArr[1];
					city = locArr[0];
					conditions.add("(BID IN (SELECT BID FROM BUSINESS WHERE STATE = ? AND CITY = ?))");
				}
				//Final Query
				String sqlQuery = "SELECT Day, MIN(OPEN), MAX(CLOSE) FROM BUSINESS_HOUR WHERE " +String.join(" AND ", conditions) + " GROUP BY DAY ORDER BY DAY";
				System.out.println("SQL QUERY : " + sqlQuery);
				PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
				int paramIndex = 1;
				for(String cat : mainCategories) {
					pstmt.setString(paramIndex, cat);
					paramIndex++;
				}
				if(subCategories != null) {
					for(String cat : subCategories) {
						pstmt.setString(paramIndex, cat);
						paramIndex++;
					}
				}
				if(attributes != null) {
					for(String cat : attributes) {
						pstmt.setString(paramIndex, cat);
						paramIndex++;
					}
				}
				
				if(location != null) {
					pstmt.setString(paramIndex, state);
					paramIndex++;
					pstmt.setString(paramIndex, city);
					paramIndex++;
				}
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()) {
					int d = rs.getInt(1);
					if(d < 0 || d > 6)
						continue;
					
					Map<String, Integer> tempTiming = new HashMap<String, Integer>();
					tempTiming.put("from",rs.getInt(2));
					tempTiming.put("to",rs.getInt(3));
					daysMap.put(d,tempTiming);
					
				}
	 			
			} catch (Exception e) {
				System.out.println("Get All Days Error " + e);
			}
			
			return daysMap;
		}
		
		public static List<List<String>> getAllBusiness(List<String> mainCategories, List<String> subCategories, List<String> attributes, String location, Integer day, Integer openingHour, Integer closingHour, String construct){
			List<List<String>> businessList = new ArrayList<List<String>>();
			if(mainCategories.isEmpty()) return businessList;
			try {
				List<String> pHolder = new ArrayList<String>();
				
				List<String> conditions = new ArrayList<String>();
				
				//Form query mainCategories - Mandatory
				int countMainCategories = mainCategories.size();
				pHolder.clear();
				while(pHolder.size() != countMainCategories) pHolder.add("?");
				
				String subQueryMainCat = "SELECT BID FROM BUSINESS_MAIN_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
				if(construct.equalsIgnoreCase("or")) {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID"; 
				} else {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countMainCategories;
				}
				conditions.add("(BID IN (" + subQueryMainCat + "))");
				
				//SubCategories Optional with AND condition other condition
				if(subCategories != null && subCategories.size() > 0) {
					int countSubCategories = subCategories.size();
					pHolder.clear();
					while(pHolder.size() != countSubCategories) pHolder.add("?");
					
					String subQuerySubCat = "SELECT BID FROM BUSINESS_SUB_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
					if(construct.equalsIgnoreCase("or")) {
						subQuerySubCat = subQuerySubCat + " GROUP BY BID"; 
					} else {
						subQuerySubCat = subQuerySubCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countSubCategories;
					}
					conditions.add("(BID IN (" + subQuerySubCat + "))");
				}
				
				//Attribute - Optional with AND condition other condition
				if(attributes != null && attributes.size() > 0) {
					int countAttributeCategories = attributes.size();
					pHolder.clear();
					while(pHolder.size() != countAttributeCategories) pHolder.add("?");
					
					String subQueryAttributeCat = "SELECT BID FROM BUSINESS_ATTRIBUTE WHERE ATTRIBUTE IN (" + String.join(",", pHolder) + ")";
					if(construct.equalsIgnoreCase("or")) {
						subQueryAttributeCat = subQueryAttributeCat + " GROUP BY BID"; 
					} else {
						subQueryAttributeCat = subQueryAttributeCat + " GROUP BY BID HAVING COUNT(ATTRIBUTE) = " + countAttributeCategories;
					}
					conditions.add("(BID IN (" + subQueryAttributeCat + "))");
				}
				
				//Location
				String state = null;
				String city = null;
				if(location != null) {
					String[] locArr = location.split(",");
					state = locArr[1];
					city = locArr[0];
					conditions.add("(STATE = ? AND CITY = ?)");
				}
				String checkInCountQuery = "SELECT SUM(COUNT) FROM BUSINESS_CHECKIN WHERE BUSINESS.BID = BUSINESS_CHECKIN.BID";
				if(day != null) {
					int from = 0;
					int to = 24;
					if(openingHour==null) openingHour = 24*60;
					else from = Math.floorDiv(openingHour, 60);
					if(closingHour==null) closingHour = 0;
					else to = Math.floorDiv(closingHour, 60);
					conditions.add("(0 < (SELECT COUNT(BID) FROM BUSINESS_HOUR WHERE BUSINESS_HOUR.BID = BUSINESS.BID AND DAY = "+day+" AND OPEN <= " + openingHour + " AND CLOSE >= " + closingHour + "))");
					checkInCountQuery = checkInCountQuery + " AND DAY = " + day + " AND HOUR >= " + from + " AND HOUR <= " + to;
					
				}
				
				//Final Query
				//"Business Id","Name","Address","City","State","Stars","# of Reviews","# of Checkins"
				int noOfFields = 8;
				String sqlQuery = "SELECT BID, NAME, ADDRESS, CITY, STATE, REVIEW_COUNT, STARS, ("+checkInCountQuery+") FROM BUSINESS WHERE " +String.join(" AND ", conditions) + " ORDER BY NAME";
				System.out.println("SQL QUERY : " + sqlQuery);
				PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
				int paramIndex = 1;
				for(String cat : mainCategories) {
					pstmt.setString(paramIndex, cat);
					paramIndex++;
				}
				if(subCategories != null) {
					for(String cat : subCategories) {
						pstmt.setString(paramIndex, cat);
						paramIndex++;
					}
				}
				if(attributes != null) {
					for(String cat : attributes) {
						pstmt.setString(paramIndex, cat);
						paramIndex++;
					}
				}
				
				if(location != null) {
					pstmt.setString(paramIndex, state);
					paramIndex++;
					pstmt.setString(paramIndex, city);
					paramIndex++;
				}
				
				
				ResultSet rs = pstmt.executeQuery();
				int i=1;
				List<String> row;
				while(rs.next()) {
					row = new ArrayList<String>();
					row.add(String.valueOf(i));
					for(int j = 1; j <= noOfFields; j++) {
						if(j==noOfFields) {
							if(rs.getString(j) == null) row.add("0");
							else row.add(rs.getString(j));
						} else {
							row.add(rs.getString(j));
						}
					}
					i++;
					businessList.add(row);
				}
	 			
			} catch (Exception e) {
				System.out.println("Get All Business Error " + e);
			}
			
			return businessList;
		}
		
		public static List<List<String>> getReviews(String bid) {
			List<List<String>> reviewList = new ArrayList<List<String>>();
			try {
				Statement stmt = conn.createStatement();
				int noOfFields = 7;
				ResultSet rs = stmt.executeQuery("SELECT ReviewDate, Stars, ReviewText, (SELECT NAME FROM Yelp_user WHERE YELP_USER.USERID = YELP_REVIEW.USERID), USEFUL_COUNT, FUNNY_COUNT, COOL_COUNT from yelp_review WHERE BID = '" + bid + "' ORDER BY REVIEWDATE");
				
				List<String> row;
				while(rs.next()) {
					row = new ArrayList<String>();
					for(int j = 1; j <= noOfFields; j++) {
						row.add(rs.getString(j));
					}
					reviewList.add(row);
				}
			} catch(Exception e) {
				System.out.println("get Review error " + e);
			}
			return reviewList;
		}
		
		public static String escape(String value) {
			return value.replaceAll("'", "\\\\'");
		}
		
		public static void main(String args[]) {
			//Test Method for DBManager
			//init();
			//executeQuery("Select Ename From EMP");
			System.out.println(escape("apple's"));
		}
	}

}
