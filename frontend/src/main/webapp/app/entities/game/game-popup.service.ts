import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Game } from './game.model';
import { GameService } from './game.service';
@Injectable()
export class GamePopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
        private gameService: GameService
    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.gameService.find(id).subscribe(game => {
                if (game.launchDate) {
                    game.launchDate = {
                        year: game.launchDate.getFullYear(),
                        month: game.launchDate.getMonth() + 1,
                        day: game.launchDate.getDate()
                    };
                }
                this.gameModalRef(component, game);
            });
        } else {
            return this.gameModalRef(component, new Game());
        }
    }

    gameModalRef(component: Component, game: Game): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.game = game;
        modalRef.result.then(result => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
