import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ChatMessageMySuffixComponent } from './chatmessage-my-suffix.component';
import { ChatMessageMySuffixDetailComponent } from './chatmessage-my-suffix-detail.component';
import { ChatMessageMySuffixPopupComponent } from './chatmessage-my-suffix-dialog.component';
import { ChatMessageMySuffixDeletePopupComponent } from './chatmessage-my-suffix-delete-dialog.component';

import { Principal } from '../../shared';

export const chatmessageRoute: Routes = [
    {
        path: 'chatmessage-my-suffix',
        component: ChatMessageMySuffixComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chatmessage.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'chatmessage-my-suffix/:id',
        component: ChatMessageMySuffixDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chatmessage.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const chatmessagePopupRoute: Routes = [
    {
        path: 'chatmessage-my-suffix-new',
        component: ChatMessageMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chatmessage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'chatmessage-my-suffix/:id/edit',
        component: ChatMessageMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chatmessage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'chatmessage-my-suffix/:id/delete',
        component: ChatMessageMySuffixDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chatmessage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
