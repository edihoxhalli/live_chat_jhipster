import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChatSharedModule } from '../../shared';
import { ChatAdminModule } from '../../admin/admin.module';
import {
    ChatMessageMySuffixService,
    ChatMessageMySuffixPopupService,
    ChatMessageMySuffixComponent,
    ChatMessageMySuffixDetailComponent,
    ChatMessageMySuffixDialogComponent,
    ChatMessageMySuffixPopupComponent,
    ChatMessageMySuffixDeletePopupComponent,
    ChatMessageMySuffixDeleteDialogComponent,
    chatmessageRoute,
    chatmessagePopupRoute,
} from './';

const ENTITY_STATES = [
    ...chatmessageRoute,
    ...chatmessagePopupRoute,
];

@NgModule({
    imports: [
        ChatSharedModule,
        ChatAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ChatMessageMySuffixComponent,
        ChatMessageMySuffixDetailComponent,
        ChatMessageMySuffixDialogComponent,
        ChatMessageMySuffixDeleteDialogComponent,
        ChatMessageMySuffixPopupComponent,
        ChatMessageMySuffixDeletePopupComponent,
    ],
    entryComponents: [
        ChatMessageMySuffixComponent,
        ChatMessageMySuffixDialogComponent,
        ChatMessageMySuffixPopupComponent,
        ChatMessageMySuffixDeleteDialogComponent,
        ChatMessageMySuffixDeletePopupComponent,
    ],
    providers: [
        ChatMessageMySuffixService,
        ChatMessageMySuffixPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChatChatMessageMySuffixModule {}
