/*
 * Copyright 2014-2015 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wplatform.ddal.command.ddl;

import com.wplatform.ddal.command.CommandInterface;
import com.wplatform.ddal.dbobject.table.TableView;
import com.wplatform.ddal.engine.Session;
import com.wplatform.ddal.message.DbException;

/**
 * This class represents the statement
 * ALTER VIEW
 */
public class AlterView extends DefineCommand {

    private TableView view;

    public AlterView(Session session) {
        super(session);
    }

    public void setView(TableView view) {
        this.view = view;
    }

    @Override
    public int update() {
        throw DbException.getUnsupportedException("TODO");
    }

    @Override
    public int getType() {
        return CommandInterface.ALTER_VIEW;
    }

    public TableView getView() {
        return view;
    }
    
    

}
