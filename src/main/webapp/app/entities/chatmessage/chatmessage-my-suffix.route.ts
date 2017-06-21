import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ChatmessageMySuffixComponent } from './chatmessage-my-suffix.component';
import { ChatmessageMySuffixDetailComponent } from './chatmessage-my-suffix-detail.component';
import { ChatmessageMySuffixPopupComponent } from './chatmessage-my-suffix-dialog.component';
import { ChatmessageMySuffixDeletePopupComponent } from './chatmessage-my-suffix-delete-dialog.component';

import { Principal } from '../../shared';

export const chatmessageRoute: Routes = [
    {
        path: 'chatmessage-my-suffix',
        component: ChatmessageMySuffixComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chatmessage.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'chatmessage-my-suffix/:id',
        component: ChatmessageMySuffixDetailComponent,
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
        component: ChatmessageMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chatmessage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'chatmessage-my-suffix/:id/edit',
        component: ChatmessageMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chatmessage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'chatmessage-my-suffix/:id/delete',
        component: ChatmessageMySuffixDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chatmessage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
