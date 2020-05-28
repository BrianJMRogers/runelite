/*
 * Copyright (c) 2019, dillydill123 <https://github.com/dillydill123>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.inventorysetups.ui;

import net.runelite.client.plugins.inventorysetups.InventorySetup;
import net.runelite.client.plugins.inventorysetups.InventorySetupItem;
import net.runelite.client.plugins.inventorysetups.InventorySetupsPlugin;
import lombok.Getter;
import net.runelite.api.InventoryID;
import net.runelite.api.vars.InputType;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.ImageUtil;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class InventorySetupPluginPanel extends PluginPanel
{

	private static ImageIcon COMPACT_VIEW_ICON;
	private static ImageIcon COMPACT_VIEW_HOVER_ICON;
	private static ImageIcon NO_COMPACT_VIEW_ICON;
	private static ImageIcon NO_COMPACT_VIEW_HOVER_ICON;
	private static ImageIcon ADD_ICON;
	private static ImageIcon ADD_HOVER_ICON;
	private static ImageIcon BACK_ICON;
	private static ImageIcon BACK_HOVER_ICON;
	private static ImageIcon IMPORT_ICON;
	private static ImageIcon IMPORT_HOVER_ICON;
	private static ImageIcon UPDATE_ICON;
	private static ImageIcon UPDATE_HOVER_ICON;

	private static String MAIN_TITLE;

	private final JPanel noSetupsPanel;
	private final JPanel invEqPanel;
	private final JPanel overviewPanel;
	private final JScrollPane contentWrapperPane;

	private final JPanel overviewTopRightButtonsPanel;
	private final JPanel setupTopRightButtonsPanel;

	private final JLabel title;
	private final JLabel compactViewMarker;
	private final JLabel addMarker;
	private final JLabel importMarker;
	private final JLabel updateMarker;
	private final JLabel backMarker;

	private final IconTextField searchBar;

	private final InventorySetupInventoryPanel invPanel;
	private final InventorySetupEquipmentPanel eqpPanel;
	private final InventorySetupRunePouchPanel rpPanel;
	private final InventorySetupSpellbookPanel sbPanel;

	@Getter
	private InventorySetup currentSelectedSetup;

	private final InventorySetupsPlugin plugin;

	static
	{
		final BufferedImage compactIcon = ImageUtil.getResourceStreamFromClass(InventorySetupsPlugin.class, "compact_mode_icon.png");
		final BufferedImage compactIconHover = ImageUtil.luminanceOffset(compactIcon, -150);
		COMPACT_VIEW_ICON = new ImageIcon(compactIcon);
		COMPACT_VIEW_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(compactIcon, 0.53f));

		NO_COMPACT_VIEW_ICON = new ImageIcon(compactIconHover);
		NO_COMPACT_VIEW_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(compactIconHover, -100));

		final BufferedImage addIcon = ImageUtil.getResourceStreamFromClass(InventorySetupsPlugin.class, "add_icon.png");
		ADD_ICON = new ImageIcon(addIcon);
		ADD_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(addIcon, 0.53f));

		final BufferedImage importIcon = ImageUtil.getResourceStreamFromClass(InventorySetupsPlugin.class, "import_icon.png");
		IMPORT_ICON = new ImageIcon(importIcon);
		IMPORT_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(importIcon, 0.53f));

		final BufferedImage updateIcon = ImageUtil.getResourceStreamFromClass(InventorySetupsPlugin.class, "update_icon.png");
		UPDATE_ICON = new ImageIcon(updateIcon);
		UPDATE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(updateIcon, 0.53f));

		final BufferedImage backIcon = ImageUtil.getResourceStreamFromClass(InventorySetupsPlugin.class, "back_arrow_icon.png");
		BACK_ICON = new ImageIcon(backIcon);
		BACK_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(backIcon, 0.53f));

		MAIN_TITLE = "Inventory Setups";
	}

	public InventorySetupPluginPanel(final InventorySetupsPlugin plugin, final ItemManager itemManager)
	{
		super(false);
		this.currentSelectedSetup = null;
		this.plugin = plugin;
		this.rpPanel = new InventorySetupRunePouchPanel(itemManager, plugin);
		this.invPanel = new InventorySetupInventoryPanel(itemManager, plugin, rpPanel);
		this.eqpPanel = new InventorySetupEquipmentPanel(itemManager, plugin);
		this.sbPanel = new InventorySetupSpellbookPanel(itemManager, plugin);
		this.noSetupsPanel = new JPanel();
		this.invEqPanel = new JPanel();
		this.overviewPanel = new JPanel();

		// setup the title
		this.title = new JLabel();
		title.setText(MAIN_TITLE);
		title.setForeground(Color.WHITE);

		this.compactViewMarker = new JLabel(COMPACT_VIEW_ICON);
		compactViewMarker.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (SwingUtilities.isLeftMouseButton(e))
				{
					plugin.switchViews(!plugin.getConfig().compactMode());
					updateCompactViewMarker();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				compactViewMarker.setIcon(plugin.getConfig().compactMode() ? COMPACT_VIEW_HOVER_ICON : NO_COMPACT_VIEW_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				compactViewMarker.setIcon(plugin.getConfig().compactMode() ? COMPACT_VIEW_ICON : NO_COMPACT_VIEW_ICON);
			}
		});

		this.importMarker = new JLabel(IMPORT_ICON);
		importMarker.setToolTipText("Import a new inventory setup");
		importMarker.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (SwingUtilities.isLeftMouseButton(e))
				{
					plugin.importSetup();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				importMarker.setIcon(IMPORT_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				importMarker.setIcon(IMPORT_ICON);
			}
		});

		// setup the add marker (+ sign in the top right)
		this.addMarker = new JLabel(ADD_ICON);
		addMarker.setToolTipText("Add a new inventory setup");
		addMarker.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (SwingUtilities.isLeftMouseButton(e))
				{
					plugin.addInventorySetup();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				addMarker.setIcon(ADD_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				addMarker.setIcon(ADD_ICON);
			}
		});

		this.updateMarker = new JLabel(UPDATE_ICON);
		updateMarker.setToolTipText("Update setup with current inventory and equipment");
		updateMarker.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (SwingUtilities.isLeftMouseButton(e))
				{
					plugin.updateCurrentSetup(currentSelectedSetup);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				updateMarker.setIcon(UPDATE_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				updateMarker.setIcon(UPDATE_ICON);
			}
		});

		this.backMarker = new JLabel(BACK_ICON);
		backMarker.setToolTipText("Return to setups");
		backMarker.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (SwingUtilities.isLeftMouseButton(e))
				{
					returnToOverviewPanel();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				backMarker.setIcon(BACK_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				backMarker.setIcon(BACK_ICON);
			}
		});

		this.overviewTopRightButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		overviewTopRightButtonsPanel.add(compactViewMarker);
		overviewTopRightButtonsPanel.add(importMarker);
		overviewTopRightButtonsPanel.add(addMarker);
		importMarker.setBorder(new EmptyBorder(0, 8, 0, 0));
		addMarker.setBorder(new EmptyBorder(0, 8, 0, 0));

		this.setupTopRightButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		setupTopRightButtonsPanel.add(updateMarker);
		setupTopRightButtonsPanel.add(backMarker);
		backMarker.setBorder(new EmptyBorder(0, 8, 0, 0));

		// the panel on the top right that holds the buttons
		final JPanel markersPanel = new JPanel();
		markersPanel.setLayout(new FlowLayout());
		markersPanel.add(overviewTopRightButtonsPanel);
		markersPanel.add(setupTopRightButtonsPanel);
		overviewTopRightButtonsPanel.setVisible(true);
		setupTopRightButtonsPanel.setVisible(false);

		// the top panel that has the title and the buttons, and search bar
		final JPanel titleAndMarkersPanel = new JPanel();
		titleAndMarkersPanel.setLayout(new BorderLayout());
		titleAndMarkersPanel.add(title, BorderLayout.WEST);
		titleAndMarkersPanel.add(markersPanel, BorderLayout.EAST);

		this.searchBar = new IconTextField();
		searchBar.setIcon(IconTextField.Icon.SEARCH);
		searchBar.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH - 20, 30));
		searchBar.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		searchBar.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		searchBar.setMinimumSize(new Dimension(0, 30));
		searchBar.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
			}

			@Override
			public void keyPressed(KeyEvent e)
			{
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				rebuild();
			}
		});
		searchBar.addClearListener(actionEvent -> rebuild());

		// the panel that stays at the top and doesn't scroll
		// contains the title and buttons
		final JPanel northAnchoredPanel = new JPanel();
		northAnchoredPanel.setLayout(new BoxLayout(northAnchoredPanel, BoxLayout.Y_AXIS));
		northAnchoredPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
		northAnchoredPanel.add(titleAndMarkersPanel);
		northAnchoredPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		northAnchoredPanel.add(searchBar);

		// the panel that holds the inventory and equipment panels
		final BoxLayout invEqLayout = new BoxLayout(invEqPanel, BoxLayout.Y_AXIS);
		invEqPanel.setLayout(invEqLayout);
		invEqPanel.add(invPanel);
		invEqPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		invEqPanel.add(rpPanel);
		invEqPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		invEqPanel.add(eqpPanel);
		invEqPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		invEqPanel.add(sbPanel);

		// setup the error panel. It's wrapped around a normal panel
		// so it doesn't stretch to fill the parent panel
		final PluginErrorPanel errorPanel = new PluginErrorPanel();
		errorPanel.setContent("Inventory Setups", "Create an inventory setup.");
		noSetupsPanel.add(errorPanel);

		// the panel that holds the inventory panels, error panel, and the overview panel
		final JPanel contentPanel = new JPanel();
		final BoxLayout contentLayout = new BoxLayout(contentPanel, BoxLayout.Y_AXIS);
		contentPanel.setLayout(contentLayout);
		contentPanel.add(invEqPanel);
		contentPanel.add(noSetupsPanel);
		contentPanel.add(overviewPanel);

		// wrapper for the main content panel to keep it from stretching
		final JPanel contentWrapper = new JPanel(new BorderLayout());
		contentWrapper.add(Box.createGlue(), BorderLayout.CENTER);
		contentWrapper.add(contentPanel, BorderLayout.NORTH);
		this.contentWrapperPane = new JScrollPane(contentWrapper);
		this.contentWrapperPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		add(northAnchoredPanel, BorderLayout.NORTH);
		add(this.contentWrapperPane, BorderLayout.CENTER);

		// make sure the invEq panel isn't visible upon startup
		invEqPanel.setVisible(false);
		updateCompactViewMarker();
	}

	public void init(List<InventorySetup> setups)
	{
		overviewPanel.setLayout(new GridBagLayout());
		overviewPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
		updateCompactViewMarker();

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;

		for (final InventorySetup setup : setups)
		{
			InventorySetupPanel newPanel = null;
			if (plugin.getConfig().compactMode())
			{
				newPanel = new InventorySetupCompactPanel(plugin, this, setup);
			}
			else
			{
				newPanel = new InventorySetupStandardPanel(plugin, this, setup);
			}
			overviewPanel.add(newPanel, constraints);
			constraints.gridy++;

			overviewPanel.add(Box.createRigidArea(new Dimension(0, 10)), constraints);
			constraints.gridy++;
		}

		invEqPanel.setVisible(false);

		noSetupsPanel.setVisible(plugin.getInventorySetups().isEmpty());
		overviewPanel.setVisible(!plugin.getInventorySetups().isEmpty());

	}

	public void rebuild()
	{
		returnToOverviewPanel();
		overviewPanel.removeAll();

		final String text = searchBar.getText();
		List<InventorySetup> setupsToAdd = searchBar.getText().isEmpty() ? plugin.getInventorySetups() : plugin.filterSetups(searchBar.getText());
		init(setupsToAdd);

		revalidate();
		repaint();
	}

	public void refreshCurrentSetup()
	{
		if (currentSelectedSetup != null)
		{
			setCurrentInventorySetup(currentSelectedSetup, false);
		}
	}

	public void setCurrentInventorySetup(final InventorySetup inventorySetup, boolean resetScrollBar)
	{
		currentSelectedSetup = inventorySetup;
		invPanel.setSlots(inventorySetup);
		rpPanel.setSlots(inventorySetup);
		eqpPanel.setSlots(inventorySetup);
		sbPanel.setSlots(inventorySetup);

		overviewTopRightButtonsPanel.setVisible(false);
		setupTopRightButtonsPanel.setVisible(true);

		invEqPanel.setVisible(true);
		noSetupsPanel.setVisible(false);
		overviewPanel.setVisible(false);

		title.setText(inventorySetup.getName());
		searchBar.setVisible(false);

		// only show the rune pouch if the setup has a rune pouch
		rpPanel.setVisible(currentSelectedSetup.getRune_pouch() != null);

		highlightInventory();
		highlightEquipment();
		highlightSpellbook();

		if (resetScrollBar)
		{
			// reset scrollbar back to top
			this.contentWrapperPane.getVerticalScrollBar().setValue(0);
		}

		plugin.doBankSearch(InputType.SEARCH);

		validate();
		repaint();

	}

	public void highlightInventory()
	{
		// if the panel itself isn't visible, don't waste time doing any highlighting logic
		if (!invEqPanel.isVisible())
		{
			return;
		}

		// if the panel is visible, check if highlighting is enabled on the setup and globally
		// if any of the two, reset the slots so they aren't highlighted
		if (!currentSelectedSetup.isHighlightDifference() || !plugin.isHighlightingAllowed())
		{
			invPanel.resetSlotColors();
			return;
		}

		final ArrayList<InventorySetupItem> inv = plugin.getNormalizedContainer(InventoryID.INVENTORY);
		invPanel.highlightSlotDifferences(inv, currentSelectedSetup);
	}

	public void highlightEquipment()
	{
		// if the panel itself isn't visible, don't waste time doing any highlighting logic
		if (!invEqPanel.isVisible())
		{
			return;
		}

		// if the panel is visible, check if highlighting is enabled on the setup and globally
		// if any of the two, reset the slots so they aren't highlighted
		if (!currentSelectedSetup.isHighlightDifference() || !plugin.isHighlightingAllowed())
		{
			eqpPanel.resetSlotColors();
			return;
		}

		final ArrayList<InventorySetupItem> eqp = plugin.getNormalizedContainer(InventoryID.EQUIPMENT);
		eqpPanel.highlightSlotDifferences(eqp, currentSelectedSetup);
	}

	public void highlightSpellbook()
	{
		// if the panel itself isn't visible, don't waste time doing any highlighting logic
		if (!invEqPanel.isVisible())
		{
			return;
		}

		if (!currentSelectedSetup.isHighlightDifference() || !plugin.isHighlightingAllowed())
		{
			sbPanel.resetSlotColors();
			return;
		}

		// pass it a dummy container because it only needs the current selected setup
		sbPanel.highlightSlotDifferences(new ArrayList<InventorySetupItem>(), currentSelectedSetup);

	}

	public void returnToOverviewPanel()
	{
		noSetupsPanel.setVisible(false);
		invEqPanel.setVisible(false);
		overviewPanel.setVisible(true);
		overviewTopRightButtonsPanel.setVisible(true);
		setupTopRightButtonsPanel.setVisible(false);
		title.setText(MAIN_TITLE);
		currentSelectedSetup = null;
		searchBar.setVisible(true);
		plugin.resetBankSearch();
	}

	private void updateCompactViewMarker()
	{
		compactViewMarker.setIcon(plugin.getConfig().compactMode() ? COMPACT_VIEW_ICON : NO_COMPACT_VIEW_ICON);
		compactViewMarker.setToolTipText("Switch to " + (plugin.getConfig().compactMode() ? "standard mode" : "compact mode"));
	}
}
