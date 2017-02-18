import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Reprint } from './reprint.model';
import { ReprintPopupService } from './reprint-popup.service';
import { ReprintService } from './reprint.service';
import { Card, CardService } from '../card';
import { Edition, EditionService } from '../edition';
@Component({
    selector: 'jhi-reprint-dialog',
    templateUrl: './reprint-dialog.component.html'
})
export class ReprintDialogComponent implements OnInit {

    reprint: Reprint;
    authorities: any[];
    isSaving: boolean;

    cards: Card[];

    editions: Edition[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private reprintService: ReprintService,
        private cardService: CardService,
        private editionService: EditionService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['reprint']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.cardService.query().subscribe(
            (res: Response) => { this.cards = res.json(); }, (res: Response) => this.onError(res.json()));
        this.editionService.query({filter: 'reprint-is-null'}).subscribe((res: Response) => {
            if (!this.reprint.editionId) {
                this.editions = res.json();
            } else {
                this.editionService.find(this.reprint.editionId).subscribe((subRes: Edition) => {
                    this.editions = [subRes].concat(res.json());
                }, (subRes: Response) => this.onError(subRes.json()));
            }
        }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.reprint.id !== undefined) {
            this.reprintService.update(this.reprint)
                .subscribe((res: Reprint) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.reprintService.create(this.reprint)
                .subscribe((res: Reprint) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Reprint) {
        this.eventManager.broadcast({ name: 'reprintListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

    trackCardById(index: number, item: Card) {
        return item.id;
    }

    trackEditionById(index: number, item: Edition) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-reprint-popup',
    template: ''
})
export class ReprintPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private reprintPopupService: ReprintPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.reprintPopupService
                    .open(ReprintDialogComponent, params['id']);
            } else {
                this.modalRef = this.reprintPopupService
                    .open(ReprintDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
