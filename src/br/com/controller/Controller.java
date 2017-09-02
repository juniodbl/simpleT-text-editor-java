package br.com.controller;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

public class Controller 
{
	final private int width = 600;
	final private int height = 600;
	
	private JFrame frame;
	private JMenuBar barra;
	private JTextArea editorTexto;
	private JFileChooser escolhaArquivo;

	public Controller( )
	{
		criarJanelaPrincipal( );
		criarBarraMenu( );
		criarAreaEdicaoTexto( );
		exibeJanelaPrincipal( );
	}
	
	private void criarJanelaPrincipal( )
	{
		this.frame = new JFrame( "Meu Notepad" );
	
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.frame.setBounds( (int)(screenSize.getWidth() / 2 - (width/2)), (int)(screenSize.getHeight() / 2 - (height/2)), width, height);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLayout( null );
	}
	
	private void criarBarraMenu( )
	{
		barra = new JMenuBar( );
		
		JMenu menuArquivo = new JMenu( "Arquivo" );
		JMenu menuAjuda = new JMenu( "Ajuda" );
		
		JMenuItem mnSair = new JMenuItem( "Sair" );
		JMenuItem mnSalvar = new JMenuItem( "Salvar" );
		JMenuItem mnAbrir = new JMenuItem( "Abrir" );
		JMenuItem mnSobre = new JMenuItem( "Sobre" );
		
		mnSair.addActionListener( e -> System.exit( 0 ) );
		mnSalvar.addActionListener( e -> salvar( ) );
		mnSobre.addActionListener( e -> exibirMenssagemSimples( "Feito na na live! favor, curtir e compartilhar..." ) );
		mnAbrir.addActionListener( e -> exibirEscolhaArquivo( ) );
		
		menuArquivo.add( mnAbrir );
		menuArquivo.add( mnSalvar );
		menuArquivo.addSeparator();
		menuArquivo.add( mnSair );
		menuAjuda.add( mnSobre );
		
		barra.add( menuArquivo );
		barra.add( menuAjuda );
		
		this.frame.setJMenuBar( barra );
	}

	private void exibirEscolhaArquivo( )
	{
		this.escolhaArquivo = new JFileChooser( );
		escolhaArquivo.setFileSelectionMode( JFileChooser.FILES_ONLY );
		escolhaArquivo.setFileFilter(new FileFilter( ) {
			@Override
			public String getDescription() {
				return "Textos Editaveis";
			}
			
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getAbsolutePath().endsWith( ".txt" );
			}
		});
		int ret = escolhaArquivo.showOpenDialog( frame );
		
		if( ret == JFileChooser.APPROVE_OPTION )
		{
			File file = escolhaArquivo.getSelectedFile();
			try 
			{
				byte[] allBytes = Files.readAllBytes( Paths.get(file.toURI()));
				editorTexto.setText( new String( allBytes ) );
			}
			catch( IOException e )
			{
				e.printStackTrace( );
			}
		}
	}

	private void criarAreaEdicaoTexto( )
	{
		this.editorTexto = new JTextArea( );
		JScrollPane jScrollPane = new JScrollPane( this.editorTexto );
		
		this.frame.addWindowListener( new WindowListener( )
		{
			@Override
			public void windowOpened( WindowEvent e )
			{
				jScrollPane.setBounds( 0, 0, 
					width -  frame.getInsets().right - frame.getInsets().left, 
					height - frame.getInsets().bottom - frame.getInsets().top - barra.getHeight( ) );
			}
			
			@Override public void windowIconified(WindowEvent e) { }
			@Override public void windowDeiconified(WindowEvent e) { }
			@Override public void windowDeactivated(WindowEvent e) { }
			@Override public void windowClosing(WindowEvent e) { }
			@Override public void windowClosed(WindowEvent e) { }
			@Override public void windowActivated(WindowEvent e) { }
		});

		this.frame.add( jScrollPane );
	}

	private void exibeJanelaPrincipal( )
	{
		this.frame.setVisible( true );
	}

	private void exibirMenssagemSimples( String msg )
	{
		JOptionPane.showMessageDialog( null, msg );
	}

	
	
	private void salvar( )
	{
		try 
		{
			String text = editorTexto.getText();
			Files.write( Paths.get(escolhaArquivo.getSelectedFile().toURI()), text.getBytes());
		}
		catch( IOException e )
		{
			e.printStackTrace( );
		}
	}
	
	public static void main(String[] args) {
		new Controller();
	}
}