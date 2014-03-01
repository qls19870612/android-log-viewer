package org.bitbucket.mlopatkin.android.logviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

import org.bitbucket.mlopatkin.android.liblogcat.LogRecord;
import org.bitbucket.mlopatkin.android.liblogcat.LogRecord.Priority;
import org.bitbucket.mlopatkin.android.logviewer.FilteringModesPanel.ModeChangedListener;
import org.bitbucket.mlopatkin.android.logviewer.config.Configuration;
import org.bitbucket.mlopatkin.android.logviewer.search.RequestCompilationException;
import org.bitbucket.mlopatkin.android.logviewer.search.SearchStrategyFactory;

public abstract class FilterDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();

    private JTextField tagTextField;
    private JTextField messageTextField;
    private JTextField pidTextField;

    private JComboBox logLevelList;

    private FilteringModesPanel modesPanel;
    private JComboBox colorsList;

    private ModeChangedListener modeListener = new ModeChangedListener() {

        @Override
        public void modeSelected(FilteringMode mode) {
            colorsList.setVisible(mode == FilteringMode.HIGHLIGHT);
            colorsList.revalidate();
            colorsList.repaint();
        }
    };

    /**
     * Create the dialog.
     */
    protected FilterDialog(Frame owner) {
        super(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initialize();
    }

    private void initialize() {
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        {
            tagTextField = new JTextField();
            tagTextField.setColumns(10);
        }

        JLabel lblNewLabel = new JLabel("Tags to filter");

        JLabel lblMessageTextTo = new JLabel("Message text to filter");

        messageTextField = new JTextField();
        messageTextField.setColumns(10);

        JLabel lblPidsToFilter = new JLabel("PIDs or app names to filter");

        pidTextField = new JTextField();
        pidTextField.setColumns(10);

        JLabel lblLogLevel = new JLabel("Log level");

        logLevelList = new JComboBox(new PriorityComboBoxModel());

        JPanel modesWithDataPanel = new JPanel();

        colorsList = new JComboBox(new ColorsComboBoxModel());
        colorsList.setSelectedIndex(0);

        GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
        gl_contentPanel
                .setHorizontalGroup(gl_contentPanel
                        .createParallelGroup(Alignment.LEADING)
                        .addGroup(
                                gl_contentPanel
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                gl_contentPanel
                                                        .createParallelGroup(Alignment.LEADING)
                                                        .addComponent(tagTextField,
                                                                GroupLayout.DEFAULT_SIZE, 477,
                                                                Short.MAX_VALUE)
                                                        .addComponent(lblNewLabel)
                                                        .addComponent(lblMessageTextTo)
                                                        .addComponent(messageTextField,
                                                                GroupLayout.DEFAULT_SIZE, 477,
                                                                Short.MAX_VALUE)
                                                        .addComponent(lblPidsToFilter)
                                                        .addComponent(pidTextField,
                                                                GroupLayout.DEFAULT_SIZE, 477,
                                                                Short.MAX_VALUE)
                                                        .addComponent(lblLogLevel)
                                                        .addComponent(logLevelList, 0, 477,
                                                                Short.MAX_VALUE)
                                                        .addGroup(
                                                                gl_contentPanel
                                                                        .createSequentialGroup()
                                                                        .addComponent(
                                                                                modesWithDataPanel,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18)
                                                                        .addComponent(
                                                                                colorsList,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                132,
                                                                                GroupLayout.PREFERRED_SIZE)))
                                        .addContainerGap()));
        gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(
                        gl_contentPanel
                                .createSequentialGroup()
                                .addComponent(lblNewLabel)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(tagTextField, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(lblMessageTextTo)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(messageTextField, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(lblPidsToFilter)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(pidTextField, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(lblLogLevel)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(logLevelList, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                                .addGroup(
                                        gl_contentPanel
                                                .createParallelGroup(Alignment.LEADING)
                                                .addComponent(modesWithDataPanel,
                                                        Alignment.TRAILING,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addGroup(
                                                        Alignment.TRAILING,
                                                        gl_contentPanel
                                                                .createSequentialGroup()
                                                                .addComponent(colorsList,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addGap(31)))));

        modesPanel = new FilteringModesPanel();
        modesWithDataPanel.add(modesPanel);
        modesPanel.setModeChangedListener(modeListener);
        contentPanel.setLayout(gl_contentPanel);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onPositiveResult();
                    }
                });
            }
            {
                JButton cancelButton = new JButton("Cancel");
                buttonPane.add(cancelButton);
                cancelButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onNegativeResult();
                    }
                });
            }
        }
        pack();
        setLocationRelativeTo(getParent());
    }

    protected abstract void onPositiveResult();

    protected abstract void onNegativeResult();

    private static final Splitter commaSplitter =
            Splitter.on(',').trimResults(CharMatcher.WHITESPACE);

    public String[] getTags() {
        String tagsString = Strings.nullToEmpty(tagTextField.getText());
        if (!CharMatcher.WHITESPACE.matchesAllOf(tagsString)) {
            return Iterables.toArray(commaSplitter.splitToList(tagsString), String.class);
        }
        return null;
    }

    public String getMessageText() {
        String message = messageTextField.getText();
        if (!CharMatcher.WHITESPACE.matchesAllOf(message)) {
            return message;
        }
        return null;
    }

    public List<Integer> getPids() {
        String pidString = Strings.nullToEmpty(pidTextField.getText());
        if (!CharMatcher.WHITESPACE.matchesAllOf(pidString)) {
            List<Integer> pids = new ArrayList<Integer>();
            for (String pid : commaSplitter.split(pidString)) {
                try {
                    pids.add(Integer.parseInt(pid));
                } catch (NumberFormatException e) {
                    // ignore, let it go to the appName
                }
            }
            return pids;
        }
        return Collections.emptyList();
    }


    public List<String> getAppNames() {
        String pidString = pidTextField.getText();
        if (!CharMatcher.WHITESPACE.matchesAllOf(pidString)) {
            List<String> appNames = new ArrayList<String>();
            for (String item : commaSplitter.split(pidString)) {
                if (!CharMatcher.inRange('0', '9').matchesAllOf(item)) {
                    appNames.add(item);
                }
            }
            return appNames;
        } else {
            return Collections.emptyList();
        }
    }

    public LogRecord.Priority getPriority() {
        return (Priority) logLevelList.getSelectedItem();
    }

    public FilteringMode getFilteringMode() {
        return modesPanel.getSelectedMode();
    }

    private class PriorityComboBoxModel extends AbstractListModel implements ComboBoxModel {

        private Object selected;

        @Override
        public Object getSelectedItem() {
            return selected;
        }

        @Override
        public void setSelectedItem(Object anItem) {
            selected = anItem;
        }

        @Override
        public Object getElementAt(int index) {
            if (index == 0) {
                return null;
            }
            return LogRecord.Priority.values()[index - 1];
        }

        @Override
        public int getSize() {
            return LogRecord.Priority.values().length + 1;
        }

    }

    protected boolean isInputValid() {
        List<String> appNames = getAppNames();
        for (String appName : appNames) {
            try {
                SearchStrategyFactory.createSearchStrategy(appName);
            } catch (RequestCompilationException e) {
                ErrorDialogsHelper.showError(this, "%s is not a valid search expression: %s",
                        appName, e.getMessage());
                return false;
            }
        }
        String request = getMessageText();
        try {
            SearchStrategyFactory.createSearchStrategy(request);
        } catch (RequestCompilationException e) {
            ErrorDialogsHelper.showError(this, "%s is not a valid search expression: %s",
                    request, e.getMessage());
            return false;
        }
        return true;
    }

    protected JTextField getTagTextField() {
        return tagTextField;
    }

    protected JTextField getMessageTextField() {
        return messageTextField;
    }

    protected JTextField getPidTextField() {
        return pidTextField;
    }

    protected JComboBox getLogLevelList() {
        return logLevelList;
    }

    protected FilteringModesPanel getModePanel() {
        return modesPanel;
    }

    protected JComboBox getColorsList() {
        return colorsList;
    }

    private class ColorsComboBoxModel extends AbstractListModel implements ComboBoxModel {

        private Object selected;

        @Override
        public Object getSelectedItem() {
            return selected;
        }

        @Override
        public void setSelectedItem(Object anItem) {
            selected = anItem;
        }

        @Override
        public Object getElementAt(int index) {
            return "<html><span style='background-color: "
                    + toString(Configuration.ui.highlightColors().get(index)) + "'>Color " + index
                    + "</span></html>";

        }

        private String toString(Color color) {
            return String.format("#%06x", color.getRGB() & 0x00FFFFFF);
        }

        @Override
        public int getSize() {
            return Configuration.ui.highlightColors().size();
        }
    }

    public Color getSelectedColor() {
        if (getFilteringMode() == FilteringMode.HIGHLIGHT) {
            return Configuration.ui.highlightColors().get(colorsList.getSelectedIndex());
        } else {
            return null;
        }
    }

    protected void setSelectedColor(Color color) {
        int index = 0;
        for (Color current : Configuration.ui.highlightColors()) {
            if (current.equals(color)) {
                colorsList.setSelectedIndex(index);
                return;
            } else {
                ++index;
            }
        }
    }

    public Object getAdditionalData() {
        return getSelectedColor();
    }

}
