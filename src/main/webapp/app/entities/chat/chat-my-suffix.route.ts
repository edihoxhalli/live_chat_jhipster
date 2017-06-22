import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ChatMySuffixComponent } from './chat-my-suffix.component';
import { ChatMySuffixDetailComponent } from './chat-my-suffix-detail.component';
import { ChatMySuffixPopupComponent } from './chat-my-suffix-dialog.component';
import { ChatMySuffixDeletePopupComponent } from './chat-my-suffix-delete-dialog.component';
import {ChatBox} from './chat-box.component';
import {ChatBoxDetail} from './chat-box-detail.component';

import { Principal } from '../../shared';

export const chatBoxRoute: Routes = [
    {
        path: 'chat-box',
        component: ChatBox,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chat.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'chat-box/:id',
        component: ChatBoxDetail,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chat.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const chatRoute: Routes = [
    {
        path: 'chat-my-suffix',
        component: ChatMySuffixComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chat.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'chat-my-suffix/:id',
        component: ChatMySuffixDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chat.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const chatPopupRoute: Routes = [
    {
        path: 'chat-my-suffix-new',
        component: ChatMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chat.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'chat-my-suffix/:id/edit',
        component: ChatMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chat.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'chat-my-suffix/:id/delete',
        component: ChatMySuffixDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chat.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
