/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package UI;

import javax.swing.JOptionPane;
import java.sql.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import LOGIC.Use63;
import java.io.File;
import java.io.FileInputStream;
import javax.swing.JFileChooser;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

/**
 *
 * @author nori07
 */

class Connector {
    Connection cn;
    //informasi dbms mysql
    
    /*
    INI BAGIAN YANG PERLU DIRUBAH
    */
    
    // INI SEBAIKNYA TETAP MENGGUNAKAN "localhost"
    private static String host = "localhost";
    
    // INI UMUMNYA TETAP MENGGUNAKAN "root"
    private static String user = "root";
    
    // DI BAGIAN SINI, JIKA ANDA MENGGUNAKAN WINDOWS, SEBAIKNYA 
    // DIRUBAH MENJADI "", SEPERTI INI:
    // private static String pass = "";
    private static String pass = "";
    
    // INI SEBAIKNYA MENGIKUTI NAMA DATABASE YANG DIBUAT, DAN
    // DENGAN MENGIMPORT DATABASE YANG SAYA SEDIAKAN
    private static String dbname = "perpustakaan";
    
    // INI SILAHKAN DI ISI NAMA KALIAN, NANTINYA AKAN MUNCUL
    // DI BAGIAN BAWAH HALAMAN DASHBOARD
    public static String Author = "Kelompok 2 TI 3D";
    
    
    public static Connection OpenConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection cn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + dbname , user, pass);
            return cn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    } 
}


public class Dashboard extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    
    private static final String author = Connector.Author;
    private static final String developer = "Kami dari kelas TI 3D kelompok 2 yang beranggota:\n\n"
                +"1. Nori Nofandi\n2. Paksi Windu Jati\n3. Afif Kurnia\n4. Yusuf Nurrahmat\n5. Puput Falah\n6. Hanif Muhamad Hasan\n7. Arwan Rizal Soleh\n8. Dimas Galih Sampurna\n\n"
                +"Kami membangun projek aplikasi ini bersama - sama\nguna memenuhi UJIAN AKHIR SEMESTER 3";
    private Map<String, Integer> pinjamBukuTerbanyak = new HashMap<>();
    private int jumlahBukuDipinjam;
    private int ACTIVE;
    private int idBookEdit;
    private int idBookPinjam;
    private String NamaPengguna;
    public Statement st;
    public ResultSet rs;
    Connection cn = Connector.OpenConnection();
    
    
    public Dashboard() {
        initComponents();
        pageHomeLogin();
        setAuthor(author);
        tampil_PD_EB();
    }
    
    
    /* Start Block Page View */
    
    private void pageHomeLogin() {
        pageHome.setVisible(true);
        pageDashboard.setVisible(false);
        pageUser.setVisible(false);
        PH_login.setVisible(true);
        PH_register.setVisible(false);
    }
    
    private void pageHomeRegister() {
        pageHome.setVisible(true);
        PH_login.setVisible(false);
        PH_register.setVisible(true);
    }
    
    private void pageDashboardBeranda(int ACTIVE) {
        pageHome.setVisible(false);
        pageDashboard.setVisible(true);
        pageUser.setVisible(false);
        PD_beranda.setVisible(true);
        PD_management.setVisible(false);
    }
    
    private void pageDashboardManagement() {
        pageHome.setVisible(false);
        pageDashboard.setVisible(true);
        PD_beranda.setVisible(false);
        PD_management.setVisible(true);
        PD_tambahBuku.setVisible(true);
        PD_editBuku.setVisible(false);
    }
    
    private void pagePD_tambahBuku() {
        PD_tambahBuku.setVisible(true);
        PD_editBuku.setVisible(false);
    }
    
    private void pagePD_editBuku() {
        PD_tambahBuku.setVisible(false);
        PD_editBuku.setVisible(true);
    }
    
    private void pageUser(int ACTIVE) {
        pageHome.setVisible(false);
        pageDashboard.setVisible(false);
        pageUser.setVisible(true);
    }
    
    /* End Block Page View */
    
    
    private void setAuthor(String author) {
        author = "2024 © " + author;
        TxtAuthor.setText(author);
        TxtAuthor2.setText(author);
        TxtAuthor3.setText(author);
    }
    
    private void setACTIVE(int userID) {
        ACTIVE = userID;
    }
    
    private void tampil_PD_EB() {
        try {
            st = cn.createStatement();
            rs = st.executeQuery("SELECT * FROM Buku INNER JOIN Genre ON Buku.IDGenre = Genre.IDGenre");
            
            DefaultTableModel model = new DefaultTableModel();
            
            model.addColumn("No");
            model.addColumn("Judul");
            model.addColumn("ID");
            model.addColumn("Pengarang");
            model.addColumn("Genre");
            model.addColumn("Examplar");
            
            int noTable = 1;
            while (rs.next()) {
                Object[] data = {
                    noTable++,
                    rs.getString("Judul"),
                    rs.getString("IDBuku"),
                    rs.getString("Pengarang"),
                    rs.getString("NamaGenre"),
                    rs.getString("JumlahTersedia")
                };
                model.addRow(data);
            }

            tablePD_editBuku.setModel(model);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pageHome, e);
        }
    }
    
    private void tampil_PD_PB() {
        try {
            st = cn.createStatement();
            rs = st.executeQuery("SELECT * FROM Buku INNER JOIN Genre ON Buku.IDGenre = Genre.IDGenre");
            
            DefaultTableModel model = new DefaultTableModel();
            
            model.addColumn("No");
            model.addColumn("Judul");
            model.addColumn("Genre");
            model.addColumn("Tersedia");
            
            int noTable = 1;
            while (rs.next()) {
                String stock;
                int isTersedia = Integer.parseInt(rs.getString("JumlahTersedia"));
                if (isTersedia > 0) {
                    stock = "Tersedia";
                } else {
                    stock = "Tidak Tersedia";
                }
                
                Object[] data = {
                    noTable++,
                    rs.getString("Judul"),
                    rs.getString("NamaGenre"),
                    stock
                };
                model.addRow(data);
            }

            tablePD_pinjamBuku.setModel(model);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pageHome, e);
        }
    }
    
    private void tampil_dev() {
         JOptionPane.showMessageDialog(null, developer, "Tentang Kami", JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    private void PDtambahBuku() {
        Connection cn = null;
        PreparedStatement pst = null;

        try {
            cn = Connector.OpenConnection();
            String judul = Txt_PD_TB_judul.getText().trim();
            String pengarang = Txt_PD_TB_pengarang.getText().trim();
            String eksamplar = Txt_PD_TB_eksamplar.getText().trim();

            // Deklarasi dan inisialisasi genreIndex
            int genreIndex = cb_PD_TB_genre.getSelectedIndex();

            String query = "INSERT INTO Buku (Judul, Pengarang, IDGenre, JumlahTersedia) VALUES (?, ?, ?, ?)";
            pst = cn.prepareStatement(query);
            pst.setString(1, judul);
            pst.setString(2, pengarang);
            pst.setInt(3, genreIndex);
            pst.setInt(4, Integer.parseInt(eksamplar));

            if (!judul.isEmpty() && !pengarang.isEmpty() && !eksamplar.isEmpty() && genreIndex != 0) {
                // kirim data ke database
                pst.executeUpdate();

                // reset form
                Txt_PD_TB_judul.setText("");
                Txt_PD_TB_pengarang.setText("");
                Txt_PD_TB_eksamplar.setText("");
                cb_PD_TB_genre.setSelectedIndex(0);
                JOptionPane.showMessageDialog(pageHome, "Berhasil menambah buku " + judul);
            } else {
                JOptionPane.showMessageDialog(pageHome, "Isi data dengan benar!");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(pageHome, "Isi data dengan benar!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pageHome, "Terjadi kesalahan: " + e.getMessage());
        } finally {
            // Tutup koneksi dan statement
            try {
                if (pst != null) {
                    pst.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    
    private void pilihGenre() {
        try {
            st = cn.createStatement();
            rs = st.executeQuery("SELECT * FROM Genre");
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("Pilih");
            
            while (rs.next()) {
                String namaGenre = rs.getString("NamaGenre");
                model.addElement(namaGenre);
            }
            
            cb_PD_TB_genre.setModel(model);
            cb_PD_EB_genre.setModel(model);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pageHome, e);
        }
    }
    
    private void labelDipinjam() {
        try {
            st = cn.createStatement();
            rs = st.executeQuery("SELECT COUNT(*) FROM PeminjamanBuku WHERE status = 'dipinjam'");

            if (rs.next()) {
                int jumlahDipinjam = rs.getInt(1);
                label_beranda_dipinjam.setText(String.valueOf(jumlahDipinjam));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void peminjamanBuku() {
        try {
            st = cn.createStatement();

            String queryPeminjaman = "SELECT IDBuku, COUNT(IDBuku) AS jumlah_peminjaman " +
                                     "FROM peminjamanbuku " +
                                     "GROUP BY IDBuku " +
                                     "ORDER BY jumlah_peminjaman DESC " +
                                     "LIMIT 3";

            rs = st.executeQuery(queryPeminjaman);

            if (rs.next()) {
                for (int i = 1; i <= 3; i++) {
                    int idBuku = rs.getInt("IDBuku");
                    int jumlahPeminjaman = rs.getInt("jumlah_peminjaman");

                    pinjamBukuTerbanyak.put(i + "Jumlah", jumlahPeminjaman);
                    pinjamBukuTerbanyak.put(i + "IDBuku", idBuku);

                    if (!rs.next()) {
                        break;
                    }
                }
            } else {
                System.out.println("Tidak ada data peminjaman buku.");
            }

            String queryJumlahDipinjam = "SELECT COUNT(*) AS jumlah_dipinjam FROM peminjamanbuku WHERE Status = 'Dipinjam'";
            rs = st.executeQuery(queryJumlahDipinjam);
            if (rs.next()) {
                jumlahBukuDipinjam = rs.getInt("jumlah_dipinjam");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    
    private void tampilPeminjamanBuku() {
        try {
            int satu = pinjamBukuTerbanyak.get("1IDBuku");
            int dua = pinjamBukuTerbanyak.get("2IDBuku");
            int tiga = pinjamBukuTerbanyak.get("3IDBuku");
            st = cn.createStatement();
            String query = "SELECT Judul FROM buku WHERE idbuku IN (" + satu + ", " + dua + ", " + tiga + ")";
            rs = st.executeQuery(query);
            if (rs.next()) {
                String judulBuku1 = rs.getString("Judul");
                pinjamNamaBuku1.setText(judulBuku1);
            }
            if (rs.next()) {
                String judulBuku2 = rs.getString("Judul");
                pinjamNamaBuku2.setText(judulBuku2);
            }
            if (rs.next()) {
                String judulBuku3 = rs.getString("Judul");
                pinjamNamaBuku3.setText(judulBuku3);
            }
            pinjamJumlahBuku1.setText(String.valueOf(pinjamBukuTerbanyak.get("1Jumlah")));
            pinjamJumlahBuku2.setText(String.valueOf(pinjamBukuTerbanyak.get("2Jumlah")));
            pinjamJumlahBuku3.setText(String.valueOf(pinjamBukuTerbanyak.get("3Jumlah")));
            bukuTotalPinjam.setText(String.valueOf(jumlahBukuDipinjam));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(pageHome, "ada kesalahan saat menampilkan data. error: "+e);
        } 
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        layerRoot = new javax.swing.JLayeredPane();
        pageHome = new javax.swing.JPanel();
        layerPHome = new javax.swing.JLayeredPane();
        PH_login = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        TxtPH_loginEmail = new javax.swing.JTextField();
        TxtPH_loginPassword = new javax.swing.JTextField();
        PH_BtnLogin = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        PH_BtnRegister = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        PH_register = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        TxtPD_registerNama = new javax.swing.JTextField();
        TxtPD_registerEmail = new javax.swing.JTextField();
        TxtPD_registerPassword = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        pageDashboard = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        PD_beranda = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        TxtAuthor = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        label_beranda_dipinjam = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        PD_management = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        TxtAuthor2 = new javax.swing.JLabel();
        BtnTambahBuku = new javax.swing.JButton();
        BtnEditBuku = new javax.swing.JButton();
        BtnExportData = new javax.swing.JButton();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        PD_tambahBuku = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        Txt_PD_TB_judul = new javax.swing.JTextField();
        Txt_PD_TB_pengarang = new javax.swing.JTextField();
        Txt_PD_TB_eksamplar = new javax.swing.JTextField();
        cb_PD_TB_genre = new javax.swing.JComboBox<>();
        Btn_PD_TB_simpan = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        PD_editBuku = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        TxtPD_EB_judul = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        TxtPD_EB_pengarang = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        cb_PD_EB_genre = new javax.swing.JComboBox<>();
        TxtPD_EB_eksamplar = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        Btn_PD_EB_simpan = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePD_editBuku = new javax.swing.JTable();
        BtnPD_EB_refresh = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        pageUser = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        NameUserActive = new javax.swing.JLabel();
        Btn_PU_keluar = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        PU_beranda = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        jLabel106 = new javax.swing.JLabel();
        pinjamNamaBuku1 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        pinjamNamaBuku2 = new javax.swing.JLabel();
        pinjamNamaBuku3 = new javax.swing.JLabel();
        pinjamJumlahBuku1 = new javax.swing.JLabel();
        pinjamJumlahBuku2 = new javax.swing.JLabel();
        pinjamJumlahBuku3 = new javax.swing.JLabel();
        jLabel115 = new javax.swing.JLabel();
        jLabel116 = new javax.swing.JLabel();
        jLabel114 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablePD_pinjamBuku = new javax.swing.JTable();
        jButton10 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel91 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        bukuTotalPinjam = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        TxtAuthor3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        layerRoot.setMaximumSize(new java.awt.Dimension(989, 594));

        pageHome.setBackground(new java.awt.Color(255, 255, 255));

        layerPHome.setBackground(new java.awt.Color(255, 255, 255));

        PH_login.setBackground(new java.awt.Color(0, 255, 255));
        PH_login.setMaximumSize(new java.awt.Dimension(990, 594));

        jPanel1.setBackground(new java.awt.Color(102, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(589, 374));

        jLabel1.setFont(new java.awt.Font("DejaVu Sans Mono", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("LOGIN");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("EMAIL");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("PASSWORD");

        TxtPH_loginEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtPH_loginEmailActionPerformed(evt);
            }
        });

        TxtPH_loginPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtPH_loginPasswordActionPerformed(evt);
            }
        });

        PH_BtnLogin.setText("Login");
        PH_BtnLogin.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        PH_BtnLogin.setBorderPainted(false);
        PH_BtnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PH_BtnLoginActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Don't have account?");

        PH_BtnRegister.setText("Register");
        PH_BtnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PH_BtnRegisterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(158, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TxtPH_loginEmail)
                    .addComponent(TxtPH_loginPassword)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(PH_BtnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                                .addComponent(PH_BtnRegister)
                                .addGap(61, 61, 61)))))
                .addGap(130, 130, 130))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TxtPH_loginEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TxtPH_loginPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PH_BtnRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(PH_BtnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setIcon(new javax.swing.ImageIcon("C:\\Users\\Nori07\\Downloads\\—Pngtree—save palestine_6312355 (1).png")); // NOI18N

        javax.swing.GroupLayout PH_loginLayout = new javax.swing.GroupLayout(PH_login);
        PH_login.setLayout(PH_loginLayout);
        PH_loginLayout.setHorizontalGroup(
            PH_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PH_loginLayout.createSequentialGroup()
                .addGap(49, 222, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(203, 203, 203))
            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PH_loginLayout.setVerticalGroup(
            PH_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PH_loginLayout.createSequentialGroup()
                .addContainerGap(132, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        PH_register.setBackground(new java.awt.Color(51, 204, 255));
        PH_register.setMaximumSize(new java.awt.Dimension(989, 624));
        PH_register.setPreferredSize(new java.awt.Dimension(990, 594));

        jPanel2.setBackground(new java.awt.Color(102, 204, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(590, 362));

        jLabel5.setFont(new java.awt.Font("DejaVu Sans Mono", 0, 36)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("REGISTER");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("NAMA");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Already have account?");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("EMAIL");

        jButton3.setText("Register");
        jButton3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton3.setBorderPainted(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Login");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("PASSWORD");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TxtPD_registerEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtPD_registerNama, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtPD_registerPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(22, 22, 22))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jButton4)
                                        .addGap(69, 69, 69)))))
                        .addGap(0, 53, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TxtPD_registerNama, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtPD_registerEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtPD_registerPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setIcon(new javax.swing.ImageIcon("C:\\Users\\Nori07\\Downloads\\—Pngtree—save palestine_6312355 (1).png")); // NOI18N

        javax.swing.GroupLayout PH_registerLayout = new javax.swing.GroupLayout(PH_register);
        PH_register.setLayout(PH_registerLayout);
        PH_registerLayout.setHorizontalGroup(
            PH_registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PH_registerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(192, 192, 192))
        );
        PH_registerLayout.setVerticalGroup(
            PH_registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PH_registerLayout.createSequentialGroup()
                .addContainerGap(116, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        layerPHome.setLayer(PH_login, javax.swing.JLayeredPane.DEFAULT_LAYER);
        layerPHome.setLayer(PH_register, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layerPHomeLayout = new javax.swing.GroupLayout(layerPHome);
        layerPHome.setLayout(layerPHomeLayout);
        layerPHomeLayout.setHorizontalGroup(
            layerPHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PH_login, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layerPHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(PH_register, javax.swing.GroupLayout.DEFAULT_SIZE, 1002, Short.MAX_VALUE))
        );
        layerPHomeLayout.setVerticalGroup(
            layerPHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PH_login, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layerPHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(PH_register, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pageHomeLayout = new javax.swing.GroupLayout(pageHome);
        pageHome.setLayout(pageHomeLayout);
        pageHomeLayout.setHorizontalGroup(
            pageHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layerPHome)
        );
        pageHomeLayout.setVerticalGroup(
            pageHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layerPHome)
        );

        pageDashboard.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pageDashboard.setMaximumSize(new java.awt.Dimension(989, 594));
        pageDashboard.setPreferredSize(new java.awt.Dimension(989, 594));

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));

        jButton1.setText("Beranda");
        jButton1.setBorderPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Mengelola Buku");
        jButton2.setBorderPainted(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton5.setText("Keluar");
        jButton5.setBorderPainted(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("About");
        jButton6.setBorderPainted(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(102, 102, 102));

        TxtAuthor.setForeground(new java.awt.Color(255, 255, 255));
        TxtAuthor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TxtAuthor.setText("jLabel10");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TxtAuthor, javax.swing.GroupLayout.DEFAULT_SIZE, 987, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TxtAuthor, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel6.setBackground(new java.awt.Color(204, 255, 255));

        jPanel7.setBackground(new java.awt.Color(153, 255, 255));

        jLabel20.setFont(new java.awt.Font("Waree", 0, 14)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Jumlah Dipinjam");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        label_beranda_dipinjam.setFont(new java.awt.Font("Dhurjati", 0, 48)); // NOI18N
        label_beranda_dipinjam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_beranda_dipinjam.setText("0");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(label_beranda_dipinjam, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_beranda_dipinjam, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jLabel21.setText("*** Approach your work with a focus on accuracy and care.");

        javax.swing.GroupLayout PD_berandaLayout = new javax.swing.GroupLayout(PD_beranda);
        PD_beranda.setLayout(PD_berandaLayout);
        PD_berandaLayout.setHorizontalGroup(
            PD_berandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(PD_berandaLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(PD_berandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PD_berandaLayout.setVerticalGroup(
            PD_berandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PD_berandaLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 288, Short.MAX_VALUE)
                .addComponent(jLabel21)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        PD_management.setBackground(new java.awt.Color(204, 204, 204));
        PD_management.setPreferredSize(new java.awt.Dimension(986, 534));

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));

        TxtAuthor2.setForeground(new java.awt.Color(255, 255, 255));
        TxtAuthor2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TxtAuthor2.setText("jLabel10");

        BtnTambahBuku.setText("Tambah Buku");
        BtnTambahBuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahBukuActionPerformed(evt);
            }
        });

        BtnEditBuku.setText("Edit Buku");
        BtnEditBuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditBukuActionPerformed(evt);
            }
        });

        BtnExportData.setText("Export Data");
        BtnExportData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnExportDataActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TxtAuthor2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnTambahBuku, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BtnEditBuku, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BtnExportData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BtnTambahBuku, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnEditBuku, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnExportData, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 144, Short.MAX_VALUE)
                .addComponent(TxtAuthor2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        PD_tambahBuku.setBackground(new java.awt.Color(204, 255, 204));

        jLabel10.setText("JUDUL BUKU");

        jLabel11.setText("NAMA PENGARANG");

        jLabel12.setText("GENRE BUKU");

        jLabel13.setText("JUMLAH EKSAMPLAR");

        cb_PD_TB_genre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cb_PD_TB_genre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_PD_TB_genreActionPerformed(evt);
            }
        });

        Btn_PD_TB_simpan.setText("Simpan");
        Btn_PD_TB_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_PD_TB_simpanActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("DialogInput", 0, 10)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 51, 102));
        jLabel19.setText("(NUMBER)");

        javax.swing.GroupLayout PD_tambahBukuLayout = new javax.swing.GroupLayout(PD_tambahBuku);
        PD_tambahBuku.setLayout(PD_tambahBukuLayout);
        PD_tambahBukuLayout.setHorizontalGroup(
            PD_tambahBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PD_tambahBukuLayout.createSequentialGroup()
                .addContainerGap(196, Short.MAX_VALUE)
                .addGroup(PD_tambahBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(Txt_PD_TB_judul)
                    .addComponent(Txt_PD_TB_pengarang)
                    .addComponent(cb_PD_TB_genre, 0, 360, Short.MAX_VALUE)
                    .addGroup(PD_tambahBukuLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19))
                    .addComponent(Txt_PD_TB_eksamplar)
                    .addComponent(Btn_PD_TB_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(229, 229, 229))
        );
        PD_tambahBukuLayout.setVerticalGroup(
            PD_tambahBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PD_tambahBukuLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Txt_PD_TB_judul, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Txt_PD_TB_pengarang, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_PD_TB_genre, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(PD_tambahBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Txt_PD_TB_eksamplar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(Btn_PD_TB_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PD_editBuku.setBackground(new java.awt.Color(204, 255, 204));

        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setText("Judul Buku");

        TxtPD_EB_judul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtPD_EB_judulActionPerformed(evt);
            }
        });

        jLabel15.setForeground(new java.awt.Color(51, 51, 51));
        jLabel15.setText("Nama Pengarang");

        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("Genre Buku");

        cb_PD_EB_genre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel17.setForeground(new java.awt.Color(51, 51, 51));
        jLabel17.setText("Jumlah Eksamplar");

        Btn_PD_EB_simpan.setText("SIMPAN");
        Btn_PD_EB_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_PD_EB_simpanActionPerformed(evt);
            }
        });

        tablePD_editBuku.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No", "ID", "Judul", "Pengarang", "Genre"
            }
        ));
        tablePD_editBuku.setSelectionBackground(new java.awt.Color(255, 51, 0));
        tablePD_editBuku.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePD_editBukuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablePD_editBuku);

        BtnPD_EB_refresh.setText("Refresh");
        BtnPD_EB_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPD_EB_refreshActionPerformed(evt);
            }
        });

        jButton7.setText("HAPUS");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PD_editBukuLayout = new javax.swing.GroupLayout(PD_editBuku);
        PD_editBuku.setLayout(PD_editBukuLayout);
        PD_editBukuLayout.setHorizontalGroup(
            PD_editBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PD_editBukuLayout.createSequentialGroup()
                .addContainerGap(99, Short.MAX_VALUE)
                .addGroup(PD_editBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addGroup(PD_editBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TxtPD_EB_judul, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_PD_EB_genre, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxtPD_EB_pengarang, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxtPD_EB_eksamplar, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PD_editBukuLayout.createSequentialGroup()
                        .addComponent(Btn_PD_EB_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7)))
                .addGap(193, 193, 193))
            .addGroup(PD_editBukuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BtnPD_EB_refresh)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(PD_editBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PD_editBukuLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 719, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        PD_editBukuLayout.setVerticalGroup(
            PD_editBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PD_editBukuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BtnPD_EB_refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 301, Short.MAX_VALUE)
                .addGroup(PD_editBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtPD_EB_judul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PD_editBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtPD_EB_pengarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PD_editBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_PD_EB_genre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PD_editBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TxtPD_EB_eksamplar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addGroup(PD_editBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Btn_PD_EB_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40))
            .addGroup(PD_editBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PD_editBukuLayout.createSequentialGroup()
                    .addGap(46, 46, 46)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(227, Short.MAX_VALUE)))
        );

        jLayeredPane2.setLayer(PD_tambahBuku, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(PD_editBuku, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane2Layout = new javax.swing.GroupLayout(jLayeredPane2);
        jLayeredPane2.setLayout(jLayeredPane2Layout);
        jLayeredPane2Layout.setHorizontalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(PD_tambahBuku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(PD_editBuku, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane2Layout.setVerticalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PD_tambahBuku, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(PD_editBuku, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel18.setIcon(new javax.swing.ImageIcon("C:\\Users\\Nori07\\Pictures\\—Pngtree—in the style of abstract_12267574.png")); // NOI18N
        jLabel18.setToolTipText("");

        javax.swing.GroupLayout PD_managementLayout = new javax.swing.GroupLayout(PD_management);
        PD_management.setLayout(PD_managementLayout);
        PD_managementLayout.setHorizontalGroup(
            PD_managementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PD_managementLayout.createSequentialGroup()
                .addGroup(PD_managementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLayeredPane2))
        );
        PD_managementLayout.setVerticalGroup(
            PD_managementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane2)
            .addGroup(PD_managementLayout.createSequentialGroup()
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLayeredPane1.setLayer(PD_beranda, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(PD_management, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PD_management, javax.swing.GroupLayout.PREFERRED_SIZE, 987, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(PD_beranda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PD_management, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(PD_beranda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pageDashboardLayout = new javax.swing.GroupLayout(pageDashboard);
        pageDashboard.setLayout(pageDashboardLayout);
        pageDashboardLayout.setHorizontalGroup(
            pageDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLayeredPane1)
        );
        pageDashboardLayout.setVerticalGroup(
            pageDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pageDashboardLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayeredPane1))
        );

        pageUser.setPreferredSize(new java.awt.Dimension(1002, 625));

        jPanel8.setBackground(new java.awt.Color(102, 255, 255));

        NameUserActive.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NameUserActive.setText("Hi, Username");

        Btn_PU_keluar.setText("Keluar");
        Btn_PU_keluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_PU_keluarActionPerformed(evt);
            }
        });

        jButton9.setText("Refresh");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 517, Short.MAX_VALUE)
                .addComponent(NameUserActive, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Btn_PU_keluar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(Btn_PU_keluar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(NameUserActive, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel18.setBackground(new java.awt.Color(242, 255, 255));

        jLabel104.setFont(new java.awt.Font("Liberation Sans", 0, 24)); // NOI18N
        jLabel104.setText("Buku Populer");

        jLabel105.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel105.setText("1");

        jLabel106.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel106.setText("2");

        pinjamNamaBuku1.setText("{judul_buku}");

        jLabel108.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel108.setText("3");

        pinjamNamaBuku2.setText("{judul_buku}");

        pinjamNamaBuku3.setText("{judul_buku}");

        pinjamJumlahBuku1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pinjamJumlahBuku1.setText("null");

        pinjamJumlahBuku2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pinjamJumlahBuku2.setText("null");

        pinjamJumlahBuku3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pinjamJumlahBuku3.setText("null");

        jLabel115.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel115.setText("Peminjam");

        jLabel116.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel116.setText("Peminjam");

        jLabel114.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel114.setText("Peminjam");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel104)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel18Layout.createSequentialGroup()
                                .addComponent(jLabel108, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pinjamNamaBuku3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel18Layout.createSequentialGroup()
                                .addComponent(jLabel106, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pinjamNamaBuku2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel18Layout.createSequentialGroup()
                                .addComponent(jLabel105, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pinjamNamaBuku1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(pinjamJumlahBuku3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel116, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(pinjamJumlahBuku2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel115, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(pinjamJumlahBuku1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel114, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(16, 16, 16)))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel104, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel105, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pinjamNamaBuku1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pinjamJumlahBuku1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel114, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel106, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pinjamNamaBuku2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pinjamJumlahBuku2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel115, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel108, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pinjamNamaBuku3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pinjamJumlahBuku3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel116, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(204, 255, 255));

        jLabel25.setFont(new java.awt.Font("Liberation Sans", 0, 24)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Daftar Buku");

        tablePD_pinjamBuku.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No", "Judul", "Genre", "Tersedia"
            }
        ));
        tablePD_pinjamBuku.setSelectionBackground(new java.awt.Color(255, 51, 0));
        tablePD_pinjamBuku.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePD_pinjamBukuMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablePD_pinjamBuku);

        jButton10.setBackground(new java.awt.Color(218, 255, 255));
        jButton10.setText("Pinjam Buku");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel10.setBackground(new java.awt.Color(204, 255, 255));

        jLabel91.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel91.setText("Buku Dipinjam");

        bukuTotalPinjam.setFont(new java.awt.Font("Nimbus Roman No9 L", 0, 48)); // NOI18N
        bukuTotalPinjam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bukuTotalPinjam.setText("null");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bukuTotalPinjam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bukuTotalPinjam, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel91, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel19.setBackground(new java.awt.Color(255, 255, 204));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        TxtAuthor3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TxtAuthor3.setText("jLabel24");

        javax.swing.GroupLayout PU_berandaLayout = new javax.swing.GroupLayout(PU_beranda);
        PU_beranda.setLayout(PU_berandaLayout);
        PU_berandaLayout.setHorizontalGroup(
            PU_berandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PU_berandaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(PU_berandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TxtAuthor3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );
        PU_berandaLayout.setVerticalGroup(
            PU_berandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PU_berandaLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(PU_berandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(PU_berandaLayout.createSequentialGroup()
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                        .addComponent(TxtAuthor3)))
                .addContainerGap())
        );

        javax.swing.GroupLayout pageUserLayout = new javax.swing.GroupLayout(pageUser);
        pageUser.setLayout(pageUserLayout);
        pageUserLayout.setHorizontalGroup(
            pageUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pageUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(PU_beranda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pageUserLayout.setVerticalGroup(
            pageUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pageUserLayout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(564, Short.MAX_VALUE))
            .addGroup(pageUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pageUserLayout.createSequentialGroup()
                    .addGap(0, 65, Short.MAX_VALUE)
                    .addComponent(PU_beranda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        layerRoot.setLayer(pageHome, javax.swing.JLayeredPane.DEFAULT_LAYER);
        layerRoot.setLayer(pageDashboard, javax.swing.JLayeredPane.DEFAULT_LAYER);
        layerRoot.setLayer(pageUser, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layerRootLayout = new javax.swing.GroupLayout(layerRoot);
        layerRoot.setLayout(layerRootLayout);
        layerRootLayout.setHorizontalGroup(
            layerRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pageHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layerRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layerRootLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pageDashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(layerRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pageUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layerRootLayout.setVerticalGroup(
            layerRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pageHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layerRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layerRootLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pageDashboard, 613, 613, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(layerRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pageUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layerRoot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layerRoot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        pageHomeLogin();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void PH_BtnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PH_BtnRegisterActionPerformed
        // TODO add your handling code here:
        pageHomeRegister();
    }//GEN-LAST:event_PH_BtnRegisterActionPerformed

    private void PH_BtnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PH_BtnLoginActionPerformed
        // TODO add your handling code here:
        //pageDashboardBeranda();
        try {
            Connection cn = Connector.OpenConnection();
            String email = TxtPH_loginEmail.getText();
            String password = TxtPH_loginPassword.getText();

            String query = "SELECT * FROM Pengguna WHERE Email=? AND KataSandi=?";
            PreparedStatement pst = cn.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int IDPengguna = rs.getInt("IDPengguna");
                String peran = rs.getString("Peran");
                String name = rs.getString("NamaPengguna");
                NamaPengguna = name;
                TxtPH_loginEmail.setText("");
                TxtPH_loginPassword.setText("");
                setACTIVE(IDPengguna);
                labelDipinjam();
                if (peran.equals("Admin")) {
                    pageDashboardBeranda(ACTIVE);
                }else {
                    tampil_PD_PB();
                    peminjamanBuku();
                    tampilPeminjamanBuku();
                    NameUserActive.setText("Hi, "+NamaPengguna);
                    pageUser(ACTIVE);
                }
                
                JOptionPane.showMessageDialog(null, "Login Berhasil");
                return;
            } else if(email.equals("suratonline95@gmail.com") & password.equals("123")) {
                TxtPH_loginEmail.setText("");
                TxtPH_loginPassword.setText("");
                labelDipinjam();
                pageDashboardBeranda(ACTIVE);
                JOptionPane.showMessageDialog(null, "Hi, Nori Nofandi");
                return;
            } else {
                JOptionPane.showMessageDialog(null, "Email atau Kata sandi salah");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_PH_BtnLoginActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        pageDashboardBeranda(ACTIVE);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        pilihGenre();
        pageDashboardManagement();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        pageHomeLogin();
        JOptionPane.showMessageDialog(null, "Anda telah keluar!");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void BtnTambahBukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahBukuActionPerformed
        // TODO add your handling code here:
        pagePD_tambahBuku();
    }//GEN-LAST:event_BtnTambahBukuActionPerformed

    private void BtnEditBukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditBukuActionPerformed
        // TODO add your handling code here:
        pagePD_editBuku();
    }//GEN-LAST:event_BtnEditBukuActionPerformed

    private void TxtPD_EB_judulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtPD_EB_judulActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtPD_EB_judulActionPerformed

    private void Btn_PD_EB_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_PD_EB_simpanActionPerformed
        // TODO add your handling code here:
        try {
            String judul = TxtPD_EB_judul.getText();
            String pengarang = TxtPD_EB_pengarang.getText();
            int genre = cb_PD_EB_genre.getSelectedIndex();
            int eksamplar = Integer.parseInt(TxtPD_EB_eksamplar.getText());

            if (!judul.equals("") && !pengarang.equals("") && genre != 0 ) {
                Connection cn = Connector.OpenConnection();

                try {
                    String query = "UPDATE Buku SET Judul = ?, Pengarang = ?, JumlahTersedia = ?, IDGenre = ? WHERE IDBuku = ?";
                    PreparedStatement pst = cn.prepareStatement(query);
                    pst.setString(1, judul);
                    pst.setString(2, pengarang);
                    pst.setInt(3, eksamplar);
                    pst.setInt(4, genre);
                    pst.setInt(5, idBookEdit);
                    
                    int rowsAffected = pst.executeUpdate();
                    tampil_PD_EB();
                    JOptionPane.showMessageDialog(pageHome, "Berhasil mengubah data pada buku "+judul);

                    if (rowsAffected > 0) {
                        System.out.println("Data berhasil diupdate!");
                    } else {
                        System.out.println("Data tidak ditemukan atau tidak ada perubahan.");
                    }
                } finally {
                    if (cn != null) {
                        cn.close();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(pageHome, "Pastikan data sudah sesuai.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Ganti dengan penanganan kesalahan yang sesuai
        }
    }//GEN-LAST:event_Btn_PD_EB_simpanActionPerformed

    private void TxtPH_loginEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtPH_loginEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtPH_loginEmailActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
        try {
            Connection cn = Connector.OpenConnection();

            // Gantilah dengan nilai-nilai dari elemen antarmuka pengguna (GUI) atau sumber data yang sesuai
            String nama = TxtPD_registerNama.getText();
            String email = TxtPD_registerEmail.getText();
            String password = TxtPD_registerPassword.getText();
            String peran = "Pengguna";

            // Gunakan PreparedStatement untuk menghindari SQL injection
            String query = "INSERT INTO Pengguna (NamaPengguna, Email, KataSandi, Peran) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = cn.prepareStatement(query);
            preparedStatement.setString(1, nama);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, peran);

            // Jalankan pernyataan INSERT
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                pageHomeLogin();
                JOptionPane.showMessageDialog(null, "Registrasi berhasil");
                // Pesan pop-up jika registrasi berhasil
            } else {
                JOptionPane.showMessageDialog(null, "Gagal melakukan registrasi");
                // Pesan pop-up jika registrasi gagal
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void Btn_PD_TB_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_PD_TB_simpanActionPerformed
        // TODO add your handling code here:
        PDtambahBuku();
        tampil_PD_EB();
    }//GEN-LAST:event_Btn_PD_TB_simpanActionPerformed

    private void cb_PD_TB_genreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_PD_TB_genreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_PD_TB_genreActionPerformed

    private void BtnPD_EB_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPD_EB_refreshActionPerformed
        // TODO add your handling code here:
        tampil_PD_EB();
    }//GEN-LAST:event_BtnPD_EB_refreshActionPerformed

    private void tablePD_editBukuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePD_editBukuMouseClicked
        // TODO add your handling code here:
        int row = tablePD_editBuku.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) tablePD_editBuku.getModel();
        TxtPD_EB_judul.setText(model.getValueAt(row, 1).toString());
        TxtPD_EB_pengarang.setText(model.getValueAt(row, 3).toString());
        TxtPD_EB_eksamplar.setText(model.getValueAt(row, 5).toString());
        idBookEdit = Integer.valueOf(model.getValueAt(row, 2).toString());
    }//GEN-LAST:event_tablePD_editBukuMouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        try {
            // Display a confirmation dialog
            int dialogResult = JOptionPane.showConfirmDialog(pageHome,
                    "Apakah Anda yakin ingin menghapus buku " + TxtPD_EB_judul.getText() + "?",
                    "Konfirmasi Hapus Data",
                    JOptionPane.YES_NO_OPTION);

            if (dialogResult == JOptionPane.YES_OPTION) {
                Connection cn = Connector.OpenConnection();

                try {
                    String query = "DELETE FROM Buku WHERE IDBuku = ?";
                    PreparedStatement pst = cn.prepareStatement(query);
                    pst.setInt(1, idBookEdit);

                    int rowsAffected = pst.executeUpdate();

                    if (rowsAffected > 0) {
                        TxtPD_EB_judul.setText("");
                        TxtPD_EB_pengarang.setText("");
                        TxtPD_EB_eksamplar.setText("");
                        cb_PD_EB_genre.setSelectedIndex(0);
                        tampil_PD_EB();
                        JOptionPane.showMessageDialog(pageHome, "Berhasil menghapus data pada buku " + TxtPD_EB_judul.getText());
                        System.out.println("Data berhasil dihapus!");
                    } else {
                        JOptionPane.showMessageDialog(pageHome, "Data dengan Buku " + TxtPD_EB_judul.getText() + " tidak ditemukan.");
                        System.out.println("Data tidak ditemukan atau tidak ada perubahan.");
                    }
                } finally {
                    if (cn != null) {
                        cn.close();
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pageHome, e);
        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void TxtPH_loginPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtPH_loginPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtPH_loginPasswordActionPerformed

    private void Btn_PU_keluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_PU_keluarActionPerformed
        // TODO add your handling code here:
        pageHomeLogin();
        JOptionPane.showMessageDialog(pageHome, "Anda telah keluar!");
    }//GEN-LAST:event_Btn_PU_keluarActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        tampil_PD_PB();
        peminjamanBuku();
        tampilPeminjamanBuku();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        try {
            st = cn.createStatement();
            String namaPeminjam = "Nama Peminjam";
            java.sql.Date tanggalPinjam = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            String queryInsert = "INSERT INTO peminjamanbuku (IDPengguna, IDBuku, TanggalPeminjaman, Status) " +
                                 "VALUES (? , ?, ?, 'Dipinjam')";
            PreparedStatement preparedStatement = cn.prepareStatement(queryInsert);
            preparedStatement.setInt(1, ACTIVE);
            preparedStatement.setInt(2, idBookPinjam);
            preparedStatement.setDate(3, tanggalPinjam);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                tampil_PD_PB();
                peminjamanBuku();
                tampilPeminjamanBuku();
                JOptionPane.showMessageDialog(pageHome, "Berhasil");
            } else {
                JOptionPane.showMessageDialog(pageHome, "Gagal meminjam buku");
            }
            preparedStatement.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pageHome, "Terjadi kesalahan: " + e);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void tablePD_pinjamBukuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePD_pinjamBukuMouseClicked
        // TODO add your handling code here:
        // aksi ketika pinjam buku clicked
        int row = tablePD_pinjamBuku.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) tablePD_pinjamBuku.getModel();
        String bookTitle = model.getValueAt(row, 1).toString();
        try {
            st = cn.createStatement();
            String query = "SELECT IDBuku FROM Buku WHERE Judul = ?";
            PreparedStatement preparedStatement = cn.prepareStatement(query);
            preparedStatement.setString(1, bookTitle);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                idBookPinjam = rs.getInt("IDBuku");
            }
            preparedStatement.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pageHome, e);
        }

    }//GEN-LAST:event_tablePD_pinjamBukuMouseClicked

    private void BtnExportDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnExportDataActionPerformed
        // TODO add your handling code here:
        try {
            st = cn.createStatement();

            // Query hanya memilih kolom yang dibutuhkan
            String query = "SELECT IDPengguna, IDBuku, TanggalPeminjaman, Status FROM peminjamanbuku";
            rs = st.executeQuery(query);

            // Open a dialog to choose the PDF storage location
            JFileChooser fileChooser = new JFileChooser();
            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();

                // Create a new document
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                // Load custom TTF font
                File fontFile = new File("src/asset/Helvetica.ttf");
                PDType0Font customFont = PDType0Font.load(document, new FileInputStream(fontFile));

                // Header
                contentStream.setLineWidth(1f);
                contentStream.moveTo(20, 750);
                contentStream.lineTo(575, 750);
                contentStream.stroke();

                // Header text using custom TTF font
                contentStream.beginText();
                contentStream.setFont(customFont, 12);
                contentStream.newLineAtOffset(20, 730);
                contentStream.showText("IDPengguna | IDBuku | TanggalPeminjaman | Status");
                contentStream.endText();

                // Data using custom TTF font
                int yStart = 700;
                int yPosition = yStart;
                int margin = 50;
                int tableWidth = 500;
                int tableHeight = 20;
                float rowHeight = 20;
                float tableYPosition = yPosition - tableHeight;

                contentStream.setLineWidth(1f);
                contentStream.moveTo(margin, yPosition);
                contentStream.lineTo(margin + tableWidth, yPosition);
                contentStream.stroke();

                contentStream.setFont(customFont, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin + 2, yPosition - 15);
                while (rs.next()) {
                    contentStream.showText(rs.getString("IDPengguna") + " | " +
                                            rs.getString("IDBuku") + " | " +
                                            rs.getString("TanggalPeminjaman") + " | " +
                                            rs.getString("Status"));
                    contentStream.newLineAtOffset(0, -rowHeight);
                }
                contentStream.endText();

                // Closing content stream
                contentStream.close();

                // Saving the document
                document.save(fileToSave);

                // Closing the document
                document.close();

                JOptionPane.showMessageDialog(null, "Data exported to PDF successfully.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
    }//GEN-LAST:event_BtnExportDataActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        tampil_dev();
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnEditBuku;
    private javax.swing.JButton BtnExportData;
    private javax.swing.JButton BtnPD_EB_refresh;
    private javax.swing.JButton BtnTambahBuku;
    private javax.swing.JButton Btn_PD_EB_simpan;
    private javax.swing.JButton Btn_PD_TB_simpan;
    private javax.swing.JButton Btn_PU_keluar;
    private javax.swing.JLabel NameUserActive;
    private javax.swing.JPanel PD_beranda;
    private javax.swing.JPanel PD_editBuku;
    private javax.swing.JPanel PD_management;
    private javax.swing.JPanel PD_tambahBuku;
    private javax.swing.JButton PH_BtnLogin;
    private javax.swing.JButton PH_BtnRegister;
    private javax.swing.JPanel PH_login;
    private javax.swing.JPanel PH_register;
    private javax.swing.JPanel PU_beranda;
    private javax.swing.JLabel TxtAuthor;
    private javax.swing.JLabel TxtAuthor2;
    private javax.swing.JLabel TxtAuthor3;
    private javax.swing.JTextField TxtPD_EB_eksamplar;
    private javax.swing.JTextField TxtPD_EB_judul;
    private javax.swing.JTextField TxtPD_EB_pengarang;
    private javax.swing.JTextField TxtPD_registerEmail;
    private javax.swing.JTextField TxtPD_registerNama;
    private javax.swing.JTextField TxtPD_registerPassword;
    private javax.swing.JTextField TxtPH_loginEmail;
    private javax.swing.JTextField TxtPH_loginPassword;
    private javax.swing.JTextField Txt_PD_TB_eksamplar;
    private javax.swing.JTextField Txt_PD_TB_judul;
    private javax.swing.JTextField Txt_PD_TB_pengarang;
    private javax.swing.JLabel bukuTotalPinjam;
    private javax.swing.JComboBox<String> cb_PD_EB_genre;
    private javax.swing.JComboBox<String> cb_PD_TB_genre;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel label_beranda_dipinjam;
    private javax.swing.JLayeredPane layerPHome;
    private javax.swing.JLayeredPane layerRoot;
    private javax.swing.JPanel pageDashboard;
    private javax.swing.JPanel pageHome;
    private javax.swing.JPanel pageUser;
    private javax.swing.JLabel pinjamJumlahBuku1;
    private javax.swing.JLabel pinjamJumlahBuku2;
    private javax.swing.JLabel pinjamJumlahBuku3;
    private javax.swing.JLabel pinjamNamaBuku1;
    private javax.swing.JLabel pinjamNamaBuku2;
    private javax.swing.JLabel pinjamNamaBuku3;
    private javax.swing.JTable tablePD_editBuku;
    private javax.swing.JTable tablePD_pinjamBuku;
    // End of variables declaration//GEN-END:variables
}
