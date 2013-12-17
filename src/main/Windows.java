package main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.DataFormatException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;
import codingThreads.ArythmeticsThread;
import codingThreads.BlockThread;
import codingThreads.HaffmanThread;
import codingThreads.ShennonThread;
import dataClasses.Interval;
import dataClasses.TableRow;

public class Windows extends JFrame {

	/**
	 * @author Yuriy Gerasimov
	 */
	private static final long serialVersionUID = 2259227987452135252L;
	private JPanel contentPane;
	private JTable table;
	private ArrayList<TableRow> pt=new ArrayList<>();
	private JTextField txtMess;
	/**
	 * @author Yuriy Gerasimov
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Windows frame = new Windows();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Windows() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 599, 383);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSymbolNumber = new JLabel("Symbol count");
		lblSymbolNumber.setBounds(192, 12, 86, 25);
		contentPane.add(lblSymbolNumber);
		
		final JSpinner spinner = new JSpinner();
		lblSymbolNumber.setLabelFor(spinner);
		spinner.setBounds(192, 37, 100, 18);
		spinner.setModel(new SpinnerNumberModel(1, 1, 100, 1));
		contentPane.add(spinner);
		
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				DefaultTableModel m=(DefaultTableModel) table.getModel();
				m.setRowCount((int)spinner.getValue());
			}
		});
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(192, 66, 91, 23);
		contentPane.add(btnStart);
		
		txtMess = new JTextField();
		txtMess.setBounds(192, 121, 86, 20);
		contentPane.add(txtMess);
		txtMess.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 12, 170, 337);
		contentPane.add(panel);
		panel.setLayout(new MigLayout("", "[182.00px,center][1px]", "[4px,grow]"));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, "cell 0 0,alignx left,aligny top");
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
			},
			new String[] {
				"Symbol", "Probability(%)"
			}
		));
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(0).setMaxWidth(60);
		table.getColumnModel().getColumn(1).setResizable(false);
		scrollPane.setViewportView(table);
		
		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(295, 11, 294, 338);
		contentPane.add(tabbedPane);
		
		final JSpinner spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(2, 2, 10, 1));
		spinner_1.setBounds(192, 175, 86, 18);
		contentPane.add(spinner_1);
		
		JLabel lblNewLabel = new JLabel("Message");
		lblNewLabel.setLabelFor(txtMess);
		lblNewLabel.setBounds(192, 100, 86, 18);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Block size");
		lblNewLabel_1.setLabelFor(spinner_1);
		lblNewLabel_1.setBounds(192, 154, 86, 14);
		contentPane.add(lblNewLabel_1);
					
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel dtm = (DefaultTableModel) table.getModel();
				pt.clear();
					try {
						for(int i=0;i<dtm.getRowCount();i++){
							String s=(String)dtm.getValueAt(i, 0);
							String c=s.trim();							 
							s=(String)dtm.getValueAt(i, 1);
							Integer p=Integer.parseInt(s.trim());
							pt.add(new TableRow(p,c));
						}
						int sum=0;
						for(TableRow r:pt)
							sum+=r.getP();
						if(sum!=100)
							throw new DataFormatException();
						else{
							Collections.sort(pt);
							List<TableRow> spt=Collections.synchronizedList(pt);
							tabbedPane.removeAll();
							ShennonThread sht=new ShennonThread(spt);
							sht.start();
							HaffmanThread ht=new HaffmanThread(spt);
							ht.start();
							int maxblock=(int)spinner_1.getValue();
							BlockThread[] bt=new BlockThread[maxblock-1];
							for(int i=0;i<bt.length;i++){
								bt[i]=new BlockThread(spt,i+2);
								bt[i].start();								
							}
							ArythmeticsThread at = null;
							if(!txtMess.getText().equals("")){
								at=new ArythmeticsThread(spt, txtMess.getText());
								at.start();
								at.join();
							}
							sht.join();
							tabbedPane.addTab("Shennon's code", new EncodingPanel(sht.getRes()));
							ht.join();
							tabbedPane.addTab("Haffman's code", new EncodingPanel(ht.getRes()));
							for(int i=0;i<bt.length;i++){
								bt[i].join();								
							}
							tabbedPane.addTab("Block code", new BlockEncodingPanel(bt));
							if(!txtMess.getText().equals(""))
								tabbedPane.addTab("Arythmetical code", new ArythmEncodingPanel(at.getRes()));
						}
					} catch (NumberFormatException e1) {
						e1.printStackTrace();
					} catch (DataFormatException e1) {
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
			}
		});	
	}
}
class EncodingPanel extends JPanel{

	/**
	 * @author Yuriy Gerasimov
	 */
	private static final long serialVersionUID = -5786967966326218089L;
	public EncodingPanel(List<TableRow> data) {
		this.setBounds(217, 154, 105, 183);
		this.setLayout(new MigLayout("", "0[grow]0", "0[grow]0"));
		JScrollPane scrollPane = new JScrollPane();
		this.add(scrollPane, "cell 0 0,grow");
		JTable table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
			},
			new String[] {
				"Symbol", "Code"
			}
		){
			/**
			 * @author Yuriy Gerasimov
			 */
			private static final long serialVersionUID = -5335579764871053553L;
			boolean[] columnEditables = new boolean[] {
				false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		}
		);
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);		
		scrollPane.setViewportView(table);
		DefaultTableModel m=(DefaultTableModel) table.getModel();
		m.setRowCount(data.size());
		for(int i=0;i<data.size();i++){
			m.setValueAt(data.get(i).getS(), i, 0);
			m.setValueAt(data.get(i).getCode(), i, 1);
		}			
	}	
}
class BlockEncodingPanel extends JPanel{
	/**
	 * @author Yuriy Gerasimov
	 */
	private static final long serialVersionUID = -3256377103242402733L;

	public BlockEncodingPanel(BlockThread[] bt) {
		super();
		this.setBounds(22, 360, 300, 243);
		this.setLayout(new MigLayout("", "0[grow]0", "0[grow]0"));
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.removeAll();
		this.add(tabbedPane, "cell 0 0,grow");
		int i=2;
		for(BlockThread t:bt){					
			JPanel panel=new JPanel();
			panel.setBounds(222, 187, 91, 150);
			panel.setLayout(new MigLayout("", "0[center]0[center]0", "0[grow]0[grow]0"));
			
			JLabel lblShennon = new JLabel("Shennon");
			panel.add(lblShennon, "cell 0 0");
			
			JLabel lblHaffman = new JLabel("Haffman");
			panel.add(lblHaffman, "cell 1 0");
			
			JScrollPane scrollPane = new JScrollPane();
			panel.add(scrollPane, "cell 0 1,grow");
			
			JTable table_1 = new JTable();
			table_1.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},
				},
				new String[] {
					"String", "Code"
				}
			) {
				/**
				 * @author Yuriy Gerasimov
				 */
				private static final long serialVersionUID = 1L;
				boolean[] columnEditables = new boolean[] {
					false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
			table_1.getColumnModel().getColumn(0).setResizable(false);
			table_1.getColumnModel().getColumn(1).setResizable(false);
			DefaultTableModel m=(DefaultTableModel) table_1.getModel();
			m.setRowCount(t.getShennon().size());
			int j=0;
			for(TableRow tr:t.getShennon()){
				m.setValueAt(tr.getS(), j, 0);
				m.setValueAt(tr.getCode(), j, 1);
				j++;
			}
			scrollPane.setViewportView(table_1);
			
			JScrollPane scrollPane_2 = new JScrollPane();
			panel.add(scrollPane_2, "cell 1 1,grow");
			
			JTable table_2 = new JTable();
			table_2.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},
				},
				new String[] {
					"String", "Code"
				}
			) {
				/**
				 * @author Yuriy Gerasimov
				 */
				private static final long serialVersionUID = 1L;
				boolean[] columnEditables = new boolean[] {
					false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
			table_2.getColumnModel().getColumn(0).setResizable(false);
			table_2.getColumnModel().getColumn(1).setResizable(false);
			scrollPane_2.setViewportView(table_2);
			m=(DefaultTableModel) table_2.getModel();
			m.setRowCount(t.getHaffman().size());
			j=0;
			for(TableRow tr:t.getHaffman()){
				m.setValueAt(tr.getS(), j, 0);
				m.setValueAt(tr.getCode(), j, 1);
				j++;
			}
			tabbedPane.addTab((i+" blocks"), panel);
			i++;
		}
	}	
}
class ArythmEncodingPanel extends JPanel{

	/**
	 * @author Yuriy Gerasimov
	 */
	private static final long serialVersionUID = 4899676143523740852L;
	public ArythmEncodingPanel(Interval i) {
		super();
		this.setBounds(192, 204, 86, 64);
		this.setLayout(new MigLayout("", "0[center]10[center]0", "0[center]0[center]0[center]0"));
		JLabel lblLeft = new JLabel("Left");
		this.add(lblLeft, "cell 0 1");
		
		JLabel leftVal = new JLabel("");
		leftVal.setText(i.getLeft().toString());
		this.add(leftVal, "cell 1 1");
		
		JLabel lblRight = new JLabel("Right");
		this.add(lblRight, "cell 0 2");
		
		JLabel rightVal = new JLabel("");
		rightVal.setText(i.getRight().toString());
		this.add(rightVal, "cell 1 2");	
	}	
}