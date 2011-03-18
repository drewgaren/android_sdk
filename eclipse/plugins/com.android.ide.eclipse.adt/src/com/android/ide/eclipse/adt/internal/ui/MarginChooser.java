/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.ide.eclipse.adt.internal.ui;

import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

public class MarginChooser extends SelectionStatusDialog implements Listener {
    private IProject mProject;
    private AndroidTargetData mTargetData;
    private Text mLeftField;
    private Text mRightField;
    private Text mTopField;
    private Text mBottomField;
    private Text mAllField;
    private String mInitialAll;
    private String mInitialLeft;
    private String mInitialRight;
    private String mInitialTop;
    private String mInitialBottom;
    private Label mErrorLabel;
    private String[] mMargins;

    // Client data key for resource buttons pointing to the associated text field
    private final static String PROP_TEXTFIELD = "textField"; //$NON-NLS-1$

    public MarginChooser(Shell parent, IProject project, AndroidTargetData targetData, String all,
            String left, String right, String top, String bottom) {
        super(parent);
        setTitle("Edit Margins");
        mProject = project;
        mTargetData = targetData;
        mInitialAll = all;
        mInitialLeft = left;
        mInitialRight = right;
        mInitialTop = top;
        mInitialBottom = bottom;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        container.setLayout(new GridLayout(3, false));

        Label allLabel = new Label(container, SWT.NONE);
        allLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        allLabel.setText("All:");

        mAllField = new Text(container, SWT.BORDER | SWT.LEFT);
        mAllField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        mAllField.setText(mInitialAll != null ? mInitialAll : ""); //$NON-NLS-1$

        Button allButton = new Button(container, SWT.NONE);
        allButton.setText("Resource...");
        allButton.setData(PROP_TEXTFIELD, mAllField);

        Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));

        Label leftLabel = new Label(container, SWT.NONE);
        leftLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        leftLabel.setText("Left:");

        mLeftField = new Text(container, SWT.BORDER | SWT.LEFT);
        mLeftField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        mLeftField.setText(mInitialLeft != null ? mInitialLeft : ""); //$NON-NLS-1$

        Button leftButton = new Button(container, SWT.NONE);
        leftButton.setText("Resource...");
        leftButton.setData(PROP_TEXTFIELD, mLeftField);

        Label rightLabel = new Label(container, SWT.NONE);
        rightLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        rightLabel.setText("Right:");

        mRightField = new Text(container, SWT.BORDER | SWT.LEFT);
        mRightField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        mRightField.setText(mInitialRight != null ? mInitialRight : ""); //$NON-NLS-1$

        Button rightButton = new Button(container, SWT.NONE);
        rightButton.setText("Resource...");
        rightButton.setData(PROP_TEXTFIELD, mRightField);

        Label topLabel = new Label(container, SWT.NONE);
        topLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        topLabel.setText("Top:");

        mTopField = new Text(container, SWT.BORDER | SWT.LEFT);
        mTopField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        mTopField.setText(mInitialTop != null ? mInitialTop : ""); //$NON-NLS-1$

        Button topButton = new Button(container, SWT.NONE);
        topButton.setText("Resource...");
        topButton.setData(PROP_TEXTFIELD, mTopField);

        Label bottomLabel = new Label(container, SWT.NONE);
        bottomLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        bottomLabel.setText("Bottom:");

        mBottomField = new Text(container, SWT.BORDER | SWT.LEFT);
        mBottomField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        mBottomField.setText(mInitialBottom != null ? mInitialBottom : ""); //$NON-NLS-1$

        Button bottomButton = new Button(container, SWT.NONE);
        bottomButton.setText("Resource...");
        bottomButton.setData(PROP_TEXTFIELD, mBottomField);

        allButton.addListener(SWT.Selection, this);
        leftButton.addListener(SWT.Selection, this);
        rightButton.addListener(SWT.Selection, this);
        topButton.addListener(SWT.Selection, this);
        bottomButton.addListener(SWT.Selection, this);

        mAllField.addListener(SWT.Modify, this);
        mLeftField.addListener(SWT.Modify, this);
        mRightField.addListener(SWT.Modify, this);
        mTopField.addListener(SWT.Modify, this);
        mBottomField.addListener(SWT.Modify, this);

        new Label(container, SWT.NONE);
        mErrorLabel = new Label(container, SWT.WRAP);
        mErrorLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

        return container;
    }

    @Override
    protected void computeResult() {
        mMargins = new String[] {
                mAllField.getText().trim(),
                mLeftField.getText().trim(), mRightField.getText().trim(),
                mTopField.getText().trim(), mBottomField.getText().trim()
        };
    }

    public String[] getMargins() {
        return mMargins;
    }

    public void handleEvent(Event event) {
        if (event.type == SWT.Modify) {
            // Text field modification -- warn about non-dip numbers
            if (event.widget instanceof Text) {
                Text text = (Text) event.widget;
                String input = text.getText().trim();
                boolean isNumber = false;
                try {
                    if (Integer.parseInt(input) > 0) {
                        isNumber = true;
                    }
                } catch (NumberFormatException nufe) {
                    // Users are allowed to enter non-numbers here, not an error
                }
                if (isNumber) {
                    String message = String.format("Hint: Use \"%1$sdip\" instead", input);
                    mErrorLabel.setText(message);
                } else {
                    mErrorLabel.setText("");
                }
            }
        } else if (event.type == SWT.Selection) {
            // Button pressed - open resource chooser
            if (event.widget instanceof Button) {
                Button button = (Button) event.widget;

                // Open a resource chooser dialog for specified resource type.
                ProjectResources projectRepository = ResourceManager.getInstance()
                        .getProjectResources(mProject);
                ResourceRepository frameworkRepository = mTargetData.getFrameworkResources();
                ResourceChooser dlg = new ResourceChooser(mProject, ResourceType.DIMEN,
                        projectRepository, frameworkRepository, getShell());
                Text text = (Text) button.getData(PROP_TEXTFIELD);
                dlg.setCurrentResource(text.getText().trim());
                if (dlg.open() == Window.OK) {
                    text.setText(dlg.getCurrentResource());
                }
            }
        }
    }
}
