import { Component, OnInit } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';

import { AccountService, Account } from 'app/core';
import {Router} from '@angular/router';
import {StateStorageService} from '../core/auth/state-storage.service';
import {LoginService} from '../core/login/login.service';
import {IDirectory} from '../shared/model/directory.model';
import {DirectoryService} from '../entities/directory/directory.service';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import { filter, map } from 'rxjs/operators';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.css']
})
export class HomeComponent implements OnInit {
    account: Account;
    password: string;
    rememberMe: boolean;
    username: string;
    credentials: any;
    directories: IDirectory[];

    constructor(
        private eventManager: JhiEventManager,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private router: Router,
        protected directoryService: DirectoryService,
        private accountService: AccountService,
    ) {
        this.credentials = {};
    }

    ngOnInit() {
        this.accountService.identity().then((account: Account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        this.loadAll();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.accountService.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.accountService.isAuthenticated();
    }

    login() {
        this.loginService
            .login({
                username: 'admin',
                password: this.password,
                rememberMe: true
            })
            .then(() => {
                this.router.navigate(['']);

                this.eventManager.broadcast({
                    name: 'authenticationSuccess',
                    content: 'Sending Authentication Success'
                });

                // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                // since login is successful, go to stored previousState and clear previousState
                const redirect = this.stateStorageService.getUrl();
                if (redirect) {
                    this.stateStorageService.storeUrl(null);
                    this.router.navigate([redirect]);
                }
            })
            .catch(() => {
            });
    }

    loadAll() {
        this.directoryService
            .query()
            .pipe(
                filter((res: HttpResponse<IDirectory[]>) => res.ok),
                map((res: HttpResponse<IDirectory[]>) => res.body)
            )
            .subscribe(
                (res: IDirectory[]) => {
                    this.directories = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        // console.log()
    }
}
