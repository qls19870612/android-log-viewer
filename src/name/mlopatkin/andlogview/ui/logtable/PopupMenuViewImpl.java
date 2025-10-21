/*
 * Copyright 2020 Mikhail Lopatkin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package name.mlopatkin.andlogview.ui.logtable;

import org.checkerframework.checker.nullness.qual.Nullable;

import name.mlopatkin.andlogview.liblogcat.LogRecord;
import name.mlopatkin.andlogview.ui.logtable.menus.CopyColAction;
import name.mlopatkin.andlogview.ui.logtable.menus.CopyUrlAction;
import name.mlopatkin.andlogview.widgets.UiHelper;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

/**
 * Implementation of {@link PopupMenuPresenter.PopupMenuView}.
 */
public class PopupMenuViewImpl implements PopupMenuPresenter.PopupMenuView {
    private final JComponent owner;
    private final int x;
    private final int y;

    protected final JPopupMenu popupMenu = new JPopupMenu();
    private final Action copyAction;
    private final CopyColAction copyColAction;
    private final CopyUrlAction copyURLAction;

    public PopupMenuViewImpl(JComponent owner, int x, int y) {
        this.owner = owner;
        this.x = x;
        this.y = y;

        copyAction = UiHelper.createActionWrapper(owner, "copy", "Copy row", "control C");
        copyColAction = new CopyColAction("Copy Cell","shift C");
        
        copyURLAction = new CopyUrlAction("Copy URL","alt C");
    }

    @Override
    public void setCopyActionEnabled(boolean enabled) {
        copyAction.setEnabled(enabled);
        if (!isEmpty()) {
            popupMenu.addSeparator();
        }
        popupMenu.add(copyAction);
    }
    
    @Override
    public void setCopyColActionEnabled(Column c, @Nullable TableRow row) {
        if (row == null) {
            return;
        }
        copyColAction.setEnabled(c.getValue(row.getRowIndex(),row.getRecord())!=null);
        copyColAction.setMouseData(c,row);
        popupMenu.add(copyColAction);
    }
    
    
    @Override
    public void setCopyURLActionEnabled(Column c, @Nullable TableRow row) {
        if (row == null) {
            return;
        }
        copyURLAction.setEnabled(c.getValue(row.getRowIndex(),row.getRecord())!=null);
        copyURLAction.setMouseData(c,row);
        popupMenu.add(copyURLAction);
    }
    
    @Override
    public void show() {
        popupMenu.show(owner, x, y);
    }

    protected boolean isEmpty() {
        return popupMenu.getComponentCount() == 0;
    }
}
