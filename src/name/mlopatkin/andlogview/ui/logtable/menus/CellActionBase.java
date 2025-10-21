package name.mlopatkin.andlogview.ui.logtable.menus;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import name.mlopatkin.andlogview.ui.logtable.Column;
import name.mlopatkin.andlogview.ui.logtable.TableRow;

/**
 * 创建人  liangsong
 * 创建时间 2024/08/20 10:07
 */
public abstract class CellActionBase extends AbstractAction {
    protected Column c;
    protected TableRow row;
    
    public CellActionBase(String name,String key) {
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(key));
    }
    
    public void setMouseData(Column c, TableRow row) {
        
        this.c = c;
        this.row = row;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
}
