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
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;
import codingThreads.BlockThread;
import codingThreads.HaffmanThread;
import codingThreads.ShennonThread;

public class Windows extends JFrame {

	/**
	 * @author Yuriy Gerasimov
	 */
	private static final long serialVersionUID = 2259227987452135252L;
	private JPanel contentPane;
	private JTable table;
	private ArrayList<TableRow> pt=new ArrayList<>();
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
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSymbolNumber = new JLabel("Symbol number");
		lblSymbolNumber.setBounds(222, 12, 139, 25);
		contentPane.add(lblSymbolNumber);
		
		final JSpinner spinner = new JSpinner();
		spinner.setBounds(222, 48, 100, 18);
		spinner.setModel(new SpinnerNumberModel(1, 1, 100, 1));
		contentPane.add(spinner);
		
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				DefaultTableModel m=(DefaultTableModel) table.getModel();
				m.setRowCount((int)spinner.getValue());
			}
		});
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 12, 196, 235);
		contentPane.add(panel);
		panel.setLayout(new MigLayout("", "[182.00px][1px]", "[4px,grow]"));
		
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
		scrollPane.setViewportView(table);
		
		JButton btnStart = new JButton("Start");
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
							ShennonThread sht=new ShennonThread(spt);
							sht.start();
							sht.join();
							HaffmanThread ht=new HaffmanThread(spt);
							ht.start();
							ht.join();
							BlockThread bt=new BlockThread(spt);
							bt.start();
							bt.join();						
							System.out.println("Shennon's code");
							for(TableRow t:sht.getRes())
								System.out.println(String.format("Symb:%s Code:%s", t.getS(),t.getCode()));
							System.out.println("Haffman's code");
							for(TableRow t:ht.getRes())
								System.out.println(String.format("Symb:%s Code:%s", t.getS(),t.getCode()));
							System.out.println("Block code");
							System.out.println("Haffman's code");
							for(TableRow t:bt.getHaffman())
								System.out.println(String.format("Symb:%s Code:%s", t.getS(),t.getCode()));
							System.out.println("Shennon's code");
							for(TableRow t:bt.getShennon())
								System.out.println(String.format("Symb:%s Code:%s", t.getS(),t.getCode()));
							System.out.println();
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
		btnStart.setBounds(222, 74, 91, 23);
		contentPane.add(btnStart);
	}
}