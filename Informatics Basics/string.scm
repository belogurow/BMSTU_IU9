;----------STRING-TRIM----------------
(define (str-tr-l x)
  (if (or (equal? (car x) #\tab) (equal? (car x) #\newline) (equal? (car x) #\space))
      (str-tr-l (cdr x))
      (list->string x)))
(define (str-tr-r x)
  (if (or (equal? (car x) #\tab) (equal? (car x) #\newline) (equal? (car x) #\space))
      (str-tr-r (cdr x))
      (list->string (reverse x))))
  
(define (string-trim-left xs)
  (str-tr-l (string->list xs)))

(define (string-trim-right xs)
  (str-tr-r (reverse (string->list xs))))

(define (string-trim xs)
  (string-trim-right (string-trim-left xs)))

;---------STRING-***FIX---------------------

(define (str-pr-sf x y)
  (if (not (null? x))
      (if (equal? (car x) (car y))
          (str-pr-sf (cdr x) (cdr y))
          (equal? (car x) (cdr y)))
      (null? x)))
        
(define (string-prefix? xs ys)
  (str-pr-sf (string->list xs) (string->list ys)))

(define (string-suffix? xs ys)
   (str-pr-sf (reverse (string->list xs)) (reverse (string->list ys))))

(define (str-in x y)
  (if (null? y)
      (not (null? y))
      (if (str-pr-sf x y)
          (str-pr-sf x y)
          (str-in x (cdr y)))))
      
(define (string-infix? xs ys)
  (str-in (string->list xs) (string->list ys)))

;------------STRING-SPLIT---------
(define (my-element? x xs)
  (if (not (null? xs))
      (if (equal? x (car xs))
          (equal? x (car xs))
          (my-element? x (cdr xs)))
      (not (null? xs))))
(define (difference xs ys)
  (if (null? xs)
      '()
      (if (my-element? (car xs) ys)
          (difference (cdr xs) ys)
          (cons (car xs) (difference (cdr xs) ys)))))

(define (str-spl x)
  (if (null? x)
      '()
      (cons (list->string (cons (car x) '())) (str-spl (cdr x)))))
           
(define (string-split xs ys)
  (if (equal? (difference (string->list xs) (string->list ys)) (string->list xs))
      (cons xs '())
      (str-spl (difference (string->list xs) (string->list ys)))))