(define (factorize y)
  (let ((a (cadr (cadr y)))
        (b (cadr (caddr y))))
    (if (equal? (caddr (cadr y)) 3)
        (if (equal? (car y) '-)
            (list '* (list '- a b) (list '+ (list 'expt a '2) (list '* a b) (list 'expt b '2)))
            (list '* (list '+ a b) (list '+ (list 'expt a '2) (list '- (list '* a b)) (list 'expt b '2))))
        (list '* (list '- a b) (list '+ a b)))))
  