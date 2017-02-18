import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Block } from './block.model';
import { BlockPopupService } from './block-popup.service';
import { BlockService } from './block.service';
import { Edition, EditionService } from '../edition';
import { Game, GameService } from '../game';
@Component({
    selector: 'jhi-block-dialog',
    templateUrl: './block-dialog.component.html'
})
export class BlockDialogComponent implements OnInit {

    block: Block;
    authorities: any[];
    isSaving: boolean;

    editions: Edition[];

    games: Game[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private blockService: BlockService,
        private editionService: EditionService,
        private gameService: GameService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['block']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.editionService.query().subscribe(
            (res: Response) => { this.editions = res.json(); }, (res: Response) => this.onError(res.json()));
        this.gameService.query().subscribe(
            (res: Response) => { this.games = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.block.id !== undefined) {
            this.blockService.update(this.block)
                .subscribe((res: Block) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.blockService.create(this.block)
                .subscribe((res: Block) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Block) {
        this.eventManager.broadcast({ name: 'blockListModification', content: 'OK'});
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

    trackEditionById(index: number, item: Edition) {
        return item.id;
    }

    trackGameById(index: number, item: Game) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-block-popup',
    template: ''
})
export class BlockPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private blockPopupService: BlockPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.blockPopupService
                    .open(BlockDialogComponent, params['id']);
            } else {
                this.modalRef = this.blockPopupService
                    .open(BlockDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
