import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChatSharedModule } from '../../shared';
import { ChatAdminModule } from '../../admin/admin.module';
import {
    ChatMySuffixService,
    ChatMySuffixPopupService,
    ChatMySuffixComponent,
    ChatMySuffixDetailComponent,
    ChatMySuffixDialogComponent,
    ChatMySuffixPopupComponent,
    ChatMySuffixDeletePopupComponent,
    ChatMySuffixDeleteDialogComponent,
    chatRoute,
    chatPopupRoute,
    chatBoxRoute,
    // importing custom component for chat entity
    ChatBox,
    ChatBoxDetail
} from './';
import { FormsModule } from '@angular/forms';

const ENTITY_STATES = [
    ...chatRoute,
    ...chatPopupRoute,
    ...chatBoxRoute
];

@NgModule({
    imports: [
        FormsModule,
        ChatSharedModule,
        ChatAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ChatMySuffixComponent,
        ChatMySuffixDetailComponent,
        ChatMySuffixDialogComponent,
        ChatMySuffixDeleteDialogComponent,
        ChatMySuffixPopupComponent,
        ChatMySuffixDeletePopupComponent,
        ChatBox,
        ChatBoxDetail
    ],
    entryComponents: [
        ChatMySuffixComponent,
        ChatMySuffixDialogComponent,
        ChatMySuffixPopupComponent,
        ChatMySuffixDeleteDialogComponent,
        ChatMySuffixDeletePopupComponent,
    ],
    providers: [
        ChatMySuffixService,
        ChatMySuffixPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChatChatMySuffixModule {}
